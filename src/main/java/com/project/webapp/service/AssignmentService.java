package com.project.webapp.service;

import com.project.webapp.entity.Assignment;
import com.project.webapp.entity.AssignmentDTO;
import com.project.webapp.entity.User;
import com.project.webapp.repository.AssignmentRepository;
import com.project.webapp.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Assignment> findAll() {
        return assignmentRepository.findAll();
    }

    public Assignment saveAssignment(User user, AssignmentDTO assignmentDTO) {
        Assignment assignment = new Assignment();
        assignment.setName(assignmentDTO.getName());
        assignment.setPoints(assignmentDTO.getPoints());
        assignment.setNum_of_attempts(assignmentDTO.getNum_of_attempts());
        assignment.setDeadline(assignmentDTO.getDeadline());
        assignment.setUser(user);
        return assignmentRepository.save(assignment);
    }

    public Assignment findById(long id) {
        Optional<Assignment> assignment = assignmentRepository.findById(id);
        return assignment.orElse(null);
    }

    public void remove(User user, long assignmentId) {

        assignmentRepository.deleteById(assignmentId);
    }

    public boolean updateAssignment(String email, long id, AssignmentDTO assignmentDTO) {
        User user = userRepository.findByEmail(email);
        Assignment assignment=assignmentRepository.findById(id).get();
        if(assignment.getUser().getId()==user.getId()){
            assignment.setName(assignmentDTO.getName());
            assignment.setPoints(assignmentDTO.getPoints());
            assignment.setNum_of_attempts(assignmentDTO.getNum_of_attempts());
            assignment.setDeadline(assignmentDTO.getDeadline());
            assignmentRepository.save(assignment);
            return true;
        } else
            return false;

    }

    public boolean deleteAssignment(String email, long id) {
        User user=userRepository.findByEmail(email);
        Assignment assignment=assignmentRepository.findById(id).get();
        if(assignment.getUser().getId() == user.getId()){
            assignmentRepository.deleteById(id);
            return true;
        }else
            return false;
    }
}
