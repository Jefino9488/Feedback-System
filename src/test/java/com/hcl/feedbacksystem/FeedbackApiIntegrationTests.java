package com.hcl.feedbacksystem;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.feedbacksystem.repository.EmployeeRepository;
import com.hcl.feedbacksystem.repository.FeedbackRepository;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FeedbackApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @BeforeEach
    void cleanUp() {
        feedbackRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    void signupAndLoginShouldReturnTokenAndAllowMeEndpoint() throws Exception {
        String email = "alice@example.com";
        signupEmployee("Alice", email, "password123", "Engineering");
        String token = loginAndGetToken(email, "password123");

        mockMvc.perform(get("/api/employees/me")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.name", is("Alice")));
    }

    @Test
    void employeesEndpointShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isForbidden());
    }

    @Test
    void feedbackToSelfShouldBeRejected() throws Exception {
        String email = "self@example.com";
        Long selfId = signupEmployee("Self", email, "password123", "QA");
        String token = loginAndGetToken(email, "password123");

        Map<String, Object> payload = Map.of(
                "rating", 5,
                "comment", "Great work",
                "givenToEmployeeId", selfId
        );

        mockMvc.perform(post("/api/feedback")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Cannot give feedback to yourself")));
    }

    @Test
    void ratingOutsideRangeShouldBeRejected() throws Exception {
        String giverEmail = "giver@example.com";
        String receiverEmail = "receiver@example.com";

        signupEmployee("Giver", giverEmail, "password123", "Engineering");
        Long receiverId = signupEmployee("Receiver", receiverEmail, "password123", "Engineering");
        String token = loginAndGetToken(giverEmail, "password123");

        Map<String, Object> payload = Map.of(
                "rating", 6,
                "comment", "Out of range",
                "givenToEmployeeId", receiverId
        );

        mockMvc.perform(post("/api/feedback")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.rating", is("Rating must be between 1 and 5")));
    }

    @Test
    void givenAndReceivedFeedbackEndpointsShouldReturnExpectedData() throws Exception {
        String aliceEmail = "alice2@example.com";
        String bobEmail = "bob2@example.com";

        Long aliceId = signupEmployee("Alice", aliceEmail, "password123", "Engineering");
        Long bobId = signupEmployee("Bob", bobEmail, "password123", "Product");

        String aliceToken = loginAndGetToken(aliceEmail, "password123");
        String bobToken = loginAndGetToken(bobEmail, "password123");

        Map<String, Object> payload = Map.of(
                "rating", 5,
                "comment", "Excellent collaboration",
                "givenToEmployeeId", bobId
        );

        mockMvc.perform(post("/api/feedback")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(aliceToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));

        mockMvc.perform(get("/api/feedback/given")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(aliceToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].givenBy.id", is(aliceId.intValue())))
                .andExpect(jsonPath("$[0].givenTo.id", is(bobId.intValue())));

        mockMvc.perform(get("/api/feedback/received")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(bobToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].givenBy.id", is(aliceId.intValue())))
                .andExpect(jsonPath("$[0].givenTo.id", is(bobId.intValue())));
    }

    private Long signupEmployee(String name, String email, String password, String department) throws Exception {
        Map<String, Object> signupPayload = Map.of(
                "name", name,
                "email", email,
                "password", password,
                "department", department
        );

        String response = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupPayload)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode root = objectMapper.readTree(response);
        return root.get("id").asLong();
    }

    private String loginAndGetToken(String email, String password) throws Exception {
        Map<String, Object> loginPayload = Map.of(
                "email", email,
                "password", password
        );

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode root = objectMapper.readTree(response);
        return root.get("accessToken").asText();
    }

    private String bearerToken(String token) {
        return "Bearer " + token;
    }
}
