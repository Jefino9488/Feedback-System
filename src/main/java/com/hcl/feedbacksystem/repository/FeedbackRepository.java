package com.hcl.feedbacksystem.repository;

import com.hcl.feedbacksystem.entity.Feedback;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @EntityGraph(attributePaths = {"givenBy", "givenTo"})
    List<Feedback> findByGivenToIdOrderByCreatedAtDesc(Long givenToId);

    @EntityGraph(attributePaths = {"givenBy", "givenTo"})
    List<Feedback> findByGivenByIdOrderByCreatedAtDesc(Long givenById);
}
