package com.project.webapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private int points;
    private int num_of_attempts;
    private LocalDateTime deadline;
    private LocalDateTime assignment_created;
    private LocalDateTime assignment_updated;
    @ManyToOne
    @JoinColumn(name="userId")
    @JsonIgnore
    private User user;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "assignment")
    @JsonIgnore
    private List<Submission> submissionList;

    public List<Submission> getSubmissionList() {
        return submissionList;
    }

    public void setSubmissionList(List<Submission> submissionList) {
        this.submissionList = submissionList;
    }

    public Assignment(){}

    public Assignment(UUID id, String name, int points, int num_of_attempts, LocalDateTime deadline, LocalDateTime assignment_created, LocalDateTime assignment_updated) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.num_of_attempts = num_of_attempts;
        this.deadline = deadline;
        this.assignment_created = assignment_created;
        this.assignment_updated = assignment_updated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @PrePersist
    public void onCreate(){
        assignment_created=LocalDateTime.now();
        assignment_updated=LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate(){
        assignment_updated=LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
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

    public LocalDateTime getAssignment_created() {
        return assignment_created;
    }

    public void setAssignment_created(LocalDateTime assignment_created) {
        this.assignment_created = assignment_created;
    }

    public LocalDateTime getAssignment_updated() {
        return assignment_updated;
    }

    public void setAssignment_updated(LocalDateTime assignment_updated) {
        this.assignment_updated = assignment_updated;
    }
}
