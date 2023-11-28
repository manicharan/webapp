package com.project.webapp.service;

import com.project.webapp.entity.*;
import com.project.webapp.repository.SubmissionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SubmissionService {
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private SnsService snsService;
    private static final Logger logger = LogManager.getLogger(SubmissionService.class);

    public Submission saveSubmission(SubmissionRequestDTO submissionRequestDTO, Assignment assignment, User user){
        logger.debug("saveSubmission method hit");
        Submission submission = new Submission();
        submission.setSubmission_url(submissionRequestDTO.getSubmission_url());
        submission.setAssignment(assignment);
        submission.setUserId(user.getId());
        Submission savedSubmission = submissionRepository.save(submission);
        int attempts = submissionRepository.findAllByAssignmentAndUserId(assignment,user.getId()).size();
        snsService.publishMessage(snsService.buildJson("Valid",user,assignment,attempts));
        logger.info("Saved submission with id {} for user {} and assignment {}",savedSubmission.getId(),user.getEmail(),assignment.getId());
        logger.debug("exiting saveSubmission method");
        return savedSubmission;
    }

    public SubmissionResponseDTO getSubmissionResponseDTO(Submission submission){
        return new SubmissionResponseDTO(submission.getId(),submission.getAssignment().getId(),submission.getSubmission_url(),submission.getSubmission_date(),submission.getSubmission_updated());
    }

    public List<Submission> findAllByUserAndAssignment(User user, Assignment assignment) {
        return submissionRepository.findAllByAssignmentAndUserId(assignment,user.getId());
    }

    public Submission findById(UUID id) {
        return submissionRepository.findById(id).orElse(null);
    }
}
