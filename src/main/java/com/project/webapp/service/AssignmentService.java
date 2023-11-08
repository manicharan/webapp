package com.project.webapp.service;

import com.project.webapp.entity.Assignment;
import com.project.webapp.entity.AssignmentDTO;
import com.project.webapp.entity.User;
import com.project.webapp.repository.AssignmentRepository;
import com.project.webapp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(AssignmentService.class);

    public List<Assignment> findAll() {
        logger.debug("AssignmentService.findAll method hit");
        logger.info("fetching {} assignments",assignmentRepository.findAll().size());
        return assignmentRepository.findAll();
    }

    public Assignment saveAssignment(User user, AssignmentDTO assignmentDTO) {
        logger.debug("saveAssignment method hit");
        Assignment assignment = new Assignment();
        assignment.setName(assignmentDTO.getName());
        assignment.setPoints((int)assignmentDTO.getPoints());
        assignment.setNum_of_attempts((int)assignmentDTO.getNum_of_attempts());
        assignment.setDeadline(assignmentDTO.getDeadline());
        assignment.setUser(user);
        Assignment savedAssignment = assignmentRepository.save(assignment);
        logger.info("Saved assignment with id {} for user {}",savedAssignment.getId(),user.getEmail());
        logger.debug("exiting saveAssignment method");
        return savedAssignment;
    }

    public Assignment findById(UUID id) {
        logger.debug("findById method entered");
        Optional<Assignment> assignment = assignmentRepository.findById(id);
        if(assignment.isPresent())
            logger.info("Found assignment with id {}",id);
        else
            logger.warn("No assignment found for id {}",id);
        logger.debug("exiting findById method");
        return assignment.orElse(null);
    }

    public boolean updateAssignment(String email, UUID id, AssignmentDTO assignmentDTO) {
        logger.debug("entering updateAssignment method");
        User user = userRepository.findByEmail(email);
        Assignment assignment=assignmentRepository.findById(id).get();
        if(assignment.getUser().getId()==user.getId()){
            assignment.setName(assignmentDTO.getName());
            assignment.setPoints((int)assignmentDTO.getPoints());
            assignment.setNum_of_attempts((int)assignmentDTO.getNum_of_attempts());
            assignment.setDeadline(assignmentDTO.getDeadline());
            Assignment savedAssignment = assignmentRepository.save(assignment);
            logger.info("updated assignment with id {}",savedAssignment.getId());
            return true;
        } else{
            logger.warn("could not update assignment, Forbidden");
            return false;
        }
    }

    public boolean deleteAssignment(String email, UUID id) {
        logger.debug("entering deleteAssignment method");
        User user=userRepository.findByEmail(email);
        Assignment assignment=assignmentRepository.findById(id).get();
        if(assignment.getUser().getId() == user.getId()){
            logger.info("Deleted assignment with id {}",id);
            assignmentRepository.deleteById(id);
            return true;
        }else {
            logger.warn("could not delete assignment, Forbidden");
            return false;
        }
    }
}
