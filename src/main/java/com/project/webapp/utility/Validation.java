package com.project.webapp.utility;

import com.project.webapp.entity.AssignmentDTO;

import java.time.LocalDateTime;

public class Validation {
    public static boolean IsValidAssignment(AssignmentDTO assignmentDTO){
        //Null validation
        if( assignmentDTO.getName().isEmpty() || assignmentDTO.getDeadline()==null ||assignmentDTO.getNum_of_attempts()<1 || assignmentDTO.getNum_of_attempts()>100)
            return false;

        if(!assignmentDTO.getName().matches(".*[a-zA-Z].*"))
            return false;
        return assignmentDTO.getPoints() >= 1 && assignmentDTO.getPoints() <= 100;
    }
}
