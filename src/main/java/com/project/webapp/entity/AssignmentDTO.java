package com.project.webapp.entity;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class AssignmentDTO {
    @NotNull(message = "name cannot be null")
    private String name;
    @NotNull(message = "points cannot be null")
    @Max(100)
    @Min(0)
    @Digits(integer = 3,fraction = 0)
    private double points;
    @NotNull
    private int num_of_attempts;
    @NotNull
    private LocalDateTime deadline;

    public AssignmentDTO(UUID id, String name, int points, int num_of_attempts, LocalDateTime deadline, LocalDateTime assignment_created, LocalDateTime assignment_updated) {
        this.name = name;
        this.points = points;
        this.num_of_attempts = num_of_attempts;
        this.deadline = deadline;
    }

    public AssignmentDTO() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public int getNum_of_attempts() {
        return num_of_attempts;
    }

    public void setNum_of_attempts(int num_of_attempts) {
        this.num_of_attempts = num_of_attempts;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
