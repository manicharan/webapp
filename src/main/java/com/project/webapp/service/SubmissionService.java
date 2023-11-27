package com.project.webapp.service;

import com.project.webapp.entity.Assignment;
import com.project.webapp.entity.Submission;
import com.project.webapp.entity.SubmissionRequestDTO;
import com.project.webapp.entity.User;
import com.project.webapp.repository.SubmissionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmissionService {
    @Autowired
    private SubmissionRepository submissionRepository;
    private static final Logger logger = LogManager.getLogger(SubmissionService.class);

    public Submission saveSubmission(SubmissionRequestDTO submissionRequestDTO, Assignment assignment, User user){
        logger.debug("saveSubmission method hit");
        Submission submission = new Submission();
        submission.setSubmission_url(submissionRequestDTO.getSubmission_url());
        submission.setAssignment(assignment);
        submission.setUserId(user.getId());
        Submission savedSubmission = submissionRepository.save(submission);
        logger.info("Saved submission with id {} for user {} and assignment {}",savedSubmission.getId(),user.getEmail(),assignment.getId());
        logger.debug("exiting saveSubmission method");
        return savedSubmission;
    }
}