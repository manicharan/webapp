package com.project.webapp.service;

import com.project.webapp.entity.Assignment;
import com.project.webapp.entity.AssignmentDTO;
import com.project.webapp.entity.User;
import com.project.webapp.repository.AssignmentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;

    public List<Assignment> findAll() {
        return assignmentRepository.findAll();
    }

    public Assignment saveAssignment(User user, AssignmentDTO assignmentDTO) {
        Assignment assignment = new Assignment();
        BeanUtils.copyProperties(assignmentDTO,assignment);
        assignment.setUser(user);
        return assignmentRepository.save(assignment);
    }

    public Assignment findById(long id) {
        Optional<Assignment> assignment = assignmentRepository.findById(id);
        return assignment.orElse(null);
    }
}
