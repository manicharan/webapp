package com.project.webapp.controller;

import com.project.webapp.entity.Assignment;
import com.project.webapp.entity.AssignmentDTO;
import com.project.webapp.entity.User;
import com.project.webapp.service.AssignmentService;
//import com.project.webapp.service.AuthenticationService;
import com.project.webapp.service.AuthenticationService;
import com.project.webapp.service.UserLoadService;
import com.project.webapp.utility.Validation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/assignments")
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private UserLoadService userLoadService;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<AssignmentDTO>> getAssignments() {
        List<Assignment> assignmentList = assignmentService.findAll();
        return new ResponseEntity<>(getAssignmentDTOsFromAssignments(assignmentList),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> getAssignmentById(@PathVariable long id) {
        Assignment assignment = assignmentService.findById(id);
        if (assignment != null)
            return new ResponseEntity<>(getAssignmentDTOFromAssignment(assignment), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<AssignmentDTO> createAssignment(@RequestBody AssignmentDTO assignmentDTO, HttpServletRequest request) {
        if (Validation.IsValidAssignment(assignmentDTO)) {
            String[] credentials = authenticationService.getCredentialsFromRequest(request);
            User user = userLoadService.findByEmail(credentials[0]);
            Assignment assignment = assignmentService.saveAssignment(user, assignmentDTO);
            return new ResponseEntity<>(getAssignmentDTOFromAssignment(assignment), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    private List<AssignmentDTO> getAssignmentDTOsFromAssignments(List<Assignment> assignmentList) {
        List<AssignmentDTO> assignmentDTOS = new ArrayList<>();
        for (Assignment a : assignmentList) {
            assignmentDTOS.add(getAssignmentDTOFromAssignment(a));
        }
        return assignmentDTOS;
    }

    private AssignmentDTO getAssignmentDTOFromAssignment(Assignment a) {
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        BeanUtils.copyProperties(a, assignmentDTO);
        return assignmentDTO;
    }

}
