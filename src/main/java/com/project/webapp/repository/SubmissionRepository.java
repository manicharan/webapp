package com.project.webapp.repository;

import com.project.webapp.entity.Assignment;
import com.project.webapp.entity.Submission;
import com.project.webapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    List<Submission> findAllByAssignmentAndUserId(Assignment assignment, UUID userId);
}
