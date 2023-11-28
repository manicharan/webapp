package com.project.webapp.controller;

import com.project.webapp.entity.*;
import com.project.webapp.service.AssignmentService;
import com.project.webapp.service.AuthenticationService;
import com.project.webapp.service.SubmissionService;
import com.project.webapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/v1/assignments/{id}/submission")
@Validated
public class SubmissionController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private SubmissionService submissionService;

    private static final Logger logger = LogManager.getLogger(SubmissionController.class);

    @PostMapping
    public ResponseEntity<Object> submitAssignment(@PathVariable UUID id, @Valid @RequestBody(required = false) SubmissionRequestDTO submissionRequestDTO, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || submissionRequestDTO == null) {
            logger.warn("Invalid body while submitting assignment at /v1/assignments/id/submission");
            return new ResponseEntity<>("Please check the request body", HttpStatus.BAD_REQUEST);
        }
        String[] credentials = authenticationService.getCredentialsFromRequest(request);
        User user = userService.findByEmail(credentials[0]);
        Assignment assignment = assignmentService.findById(id);
        if (assignment == null)
            return new ResponseEntity<>("No assignment found", HttpStatus.NOT_FOUND);
        else if (LocalDateTime.now().isAfter(assignment.getDeadline())){
            logger.info("Deadline passed, rejecting the request");
            return new ResponseEntity<>("Deadline has passed", HttpStatus.FORBIDDEN);
        }
        else if (submissionService.findByUserAndAssignment(user, assignment)!=null) {
            Submission submittedSubmission = submissionService.findByUserAndAssignment(user,assignment);
            logger.info("Found submission with id {}, updating the submission",submittedSubmission.getId());
            if(submissionService.updateSubmission(submittedSubmission.getId(),submissionRequestDTO)) {
                SubmissionResponseDTO submissionResponseDTO = submissionService.getSubmissionResponseDTO(submissionService.findById(submittedSubmission.getId()));
                return new ResponseEntity<>(submissionResponseDTO, HttpStatus.CREATED);
            }
            else
                return new ResponseEntity<>("Too many attempts", HttpStatus.TOO_MANY_REQUESTS);

        } else {
            Submission submission = submissionService.saveSubmission(submissionRequestDTO, assignment, user);
            SubmissionResponseDTO submissionResponseDTO = submissionService.getSubmissionResponseDTO(submission);
            logger.info("Submission not found, creating a new submission with id {}",submission.getId());
            return new ResponseEntity<>(submissionResponseDTO, HttpStatus.CREATED);
        }
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.HEAD, RequestMethod.OPTIONS})
    public ResponseEntity<String> anyOtherMethod() {
        logger.warn("Method not allowed at /v1/assignments/id/submission");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header("NotAllowed", "The Requested Method Is Not Allowed").cacheControl(CacheControl.noCache()).build();
    }
//    @GetMapping
//    public String testSubmission(@PathVariable UUID id){
//        return "AssignmentId: "+id;
//    }
}
