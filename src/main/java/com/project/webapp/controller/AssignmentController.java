package com.project.webapp.controller;

import com.project.webapp.entity.Assignment;
import com.project.webapp.entity.AssignmentDTO;
import com.project.webapp.entity.User;
import com.project.webapp.service.AssignmentService;
import com.project.webapp.service.AuthenticationService;
import com.project.webapp.service.UserService;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/assignments")
@Validated
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    private static final Logger logger = LogManager.getLogger(AssignmentController.class);
    private static final StatsDClient statsd = new NonBlockingStatsDClient("assignments", "localhost", 8125);



    @GetMapping
    public ResponseEntity<List<Assignment>> getAssignments() {
        statsd.incrementCounter("getAssignmentsCounter");
        List<Assignment> assignmentList = assignmentService.findAll();
        return new ResponseEntity<>(assignmentList,HttpStatus.OK);
    }
    @RequestMapping(method = {RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.PATCH,RequestMethod.HEAD,RequestMethod.OPTIONS})
    public ResponseEntity<String> anyOtherMethod(){
        logger.warn("Method not allowed at /v1/assignments");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header("NotAllowed","The Requested Method Is Not Allowed").cacheControl(CacheControl.noCache()).build();
    }

    @PostMapping
    public ResponseEntity<Object> createAssignment(@Valid @RequestBody(required = false) AssignmentDTO assignmentDTO, HttpServletRequest request, BindingResult bindingResult) {
        statsd.incrementCounter("postAssignmentCounter");
        if(bindingResult.hasErrors() || assignmentDTO==null){
            logger.warn("Invalid body while creating assignments at /v1/assignments/id");
            return new ResponseEntity<>("Please check the request body", HttpStatus.BAD_REQUEST);
        }
        String[] credentials = authenticationService.getCredentialsFromRequest(request);
        User user = userService.findByEmail(credentials[0]);
        Assignment assignment = assignmentService.saveAssignment(user, assignmentDTO);
        return new ResponseEntity<>(assignment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAssignmentById(@PathVariable UUID id) {
        statsd.incrementCounter("getAssignmentByIdCounter");
        Assignment assignment = assignmentService.findById(id);
        if (assignment != null)
            return new ResponseEntity<>(assignment, HttpStatus.OK);
        else
            return new ResponseEntity<>("The URL doesn't exist", HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAssignmentById(@PathVariable UUID id,HttpServletRequest request,@RequestBody(required = false) AssignmentDTO assignmentDTO){
        statsd.incrementCounter("deleteAssignmentCounter");
        Assignment assignment=assignmentService.findById(id);
        if(assignment!=null){
            if(assignmentDTO!=null) {
                logger.warn("Found body while deleting assignments");
                return new ResponseEntity<>("Request body is not allowed here", HttpStatus.BAD_REQUEST);
            }else {
                String[] credentials = authenticationService.getCredentialsFromRequest(request);
                if (assignmentService.deleteAssignment(credentials[0], id)) {
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                } else
                    return new ResponseEntity<>("Restricted access", HttpStatus.FORBIDDEN);
            }
        }
        else
            return new ResponseEntity<>("The URL doesn't exist",HttpStatus.NOT_FOUND);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAssignment(@PathVariable UUID id,@Valid @RequestBody(required = false) AssignmentDTO assignmentDTO, HttpServletRequest request, BindingResult bindingResult){
        statsd.incrementCounter("putAssignmentCounter");
        Assignment assignment=assignmentService.findById(id);
        if(assignment==null){
            return new ResponseEntity<>("The URL doesn't exist",HttpStatus.NOT_FOUND);
        } else {
            if (bindingResult.hasErrors() || assignmentDTO == null) {
                logger.warn("Invalid body while sending put request at /v1/assignments/id");
                return new ResponseEntity<>("Please check the request body", HttpStatus.BAD_REQUEST);
            } else {
                String[] credentials = authenticationService.getCredentialsFromRequest(request);
                if (assignmentService.updateAssignment(credentials[0], id, assignmentDTO)) {
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                } else
                    return new ResponseEntity<>("Restricted access", HttpStatus.FORBIDDEN);
            }
        }

    }
    @PatchMapping("/{id}")
    public ResponseEntity<String> patchMethod(){
        logger.warn("Patch method is not allowed here");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header("NotAllowed","The Requested Method Is Not Allowed").cacheControl(CacheControl.noCache()).build();
    }
}
