package com.project.webapp.service;

import com.project.webapp.entity.*;
import com.project.webapp.repository.SubmissionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        String message = snsService.buildJson(user,assignment);
        snsService.publishMessage(message);
        logger.info("Saved submission with id {} for user {} and assignment {}",savedSubmission.getId(),user.getEmail(),assignment.getId());
        logger.debug("exiting saveSubmission method");
        return savedSubmission;
    }

    public SubmissionResponseDTO getSubmissionResponseDTO(Submission submission){
        return new SubmissionResponseDTO(submission.getId(),submission.getAssignment().getId(),submission.getSubmission_url(),submission.getSubmission_date(),submission.getSubmission_updated());
    }

    public Submission findByUserAndAssignment(User user, Assignment assignment) {
        return submissionRepository.findByAssignmentAndUserId(assignment,user.getId());
    }

    public boolean updateSubmission(UUID id, SubmissionRequestDTO submissionRequestDTO) {
        Submission submission = submissionRepository.findById(id).get();
        if(submission.getAttempt()<submission.getAssignment().getNum_of_attempts()){
            submission.setSubmission_url(submissionRequestDTO.getSubmission_url());
            submission.setSubmission_date(LocalDateTime.now());
            submission.setSubmission_updated(LocalDateTime.now());
            submission.setAttempt(submission.getAttempt()+1);
            Submission updatedSubmission = submissionRepository.save(submission);
            logger.info("updated Submission with id {}",updatedSubmission.getId());
            return true;
        }else {
            logger.info("Exceeded number of attempts for user");
            return false;
        }
    }

    public Submission findById(UUID id) {
        return submissionRepository.findById(id).orElse(null);
    }
}
