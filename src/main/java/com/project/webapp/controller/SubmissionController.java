package com.project.webapp.controller;

import com.project.webapp.entity.*;
import com.project.webapp.service.*;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v2/assignments/{id}/submission")
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
    private static final StatsDClient statsd = new NonBlockingStatsDClient("submissions", "localhost", 8125);

    private static final Logger logger = LogManager.getLogger(SubmissionController.class);

    @PostMapping
    public ResponseEntity<Object> submitAssignment(@PathVariable UUID id, @Valid @RequestBody(required = false) SubmissionRequestDTO submissionRequestDTO, HttpServletRequest request, BindingResult bindingResult) {
        statsd.incrementCounter("postSubmissionCounter");
        if (bindingResult.hasErrors() || submissionRequestDTO == null) {
            logger.warn("Invalid body while submitting assignment at /v1/assignments/id/submission");
            return new ResponseEntity<>("Please check the request body", HttpStatus.BAD_REQUEST);
        }
        String[] credentials = authenticationService.getCredentialsFromRequest(request);
        User user = userService.findByEmail(credentials[0]);
        Assignment assignment = assignmentService.findById(id);
        if (assignment == null) {
            logger.warn("No assignment found");
            return new ResponseEntity<>("No assignment found", HttpStatus.NOT_FOUND);
        }
        else if (LocalDateTime.now().isAfter(assignment.getDeadline())){
            logger.warn("Deadline passed, rejecting the submission");
            return new ResponseEntity<>("Cannot submit, deadline has passed", HttpStatus.FORBIDDEN);
        }
        else {
            List<Submission> submissions = submissionService.findAllByUserAndAssignment(user,assignment);
            logger.info("Found {} submissions for user {}",submissions.size(),user.getEmail());
            if(submissions.size()<assignment.getNum_of_attempts()) {
                Submission savedSubmission = submissionService.saveSubmission(submissionRequestDTO,assignment,user);
                SubmissionResponseDTO submissionResponseDTO = submissionService.getSubmissionResponseDTO(savedSubmission);
                return new ResponseEntity<>(submissionResponseDTO, HttpStatus.CREATED);
            }
            else {
                logger.warn("Too many attempts, cannot submit");
                return new ResponseEntity<>("Too many attempts, cannot submit", HttpStatus.FORBIDDEN);
            }

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
