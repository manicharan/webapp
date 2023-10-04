package com.project.webapp.utility;

import com.project.webapp.entity.AssignmentDTO;

public class Validation {
    public static boolean IsValidAssignment(AssignmentDTO assignmentDTO){
        if(assignmentDTO.getNum_of_attempts()<1 || assignmentDTO.getNum_of_attempts()>100)
            return false;
        return assignmentDTO.getPoints() >= 1 && assignmentDTO.getPoints() <= 100;
    }
}
