package com.project.webapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String submission_url;
    private LocalDateTime submission_date;
    private LocalDateTime submission_updated;
    @NotNull
    private UUID userId;
    @ManyToOne
    @JoinColumn(name="assignment_id")
    @JsonIgnore
    private Assignment assignment;

    public Submission() {
    }

    public Submission(UUID id, String submission_url, LocalDateTime submission_date, LocalDateTime submission_updated, UUID userId, Assignment assignment) {
        this.id = id;
        this.submission_url = submission_url;
        this.submission_date = submission_date;
        this.submission_updated = submission_updated;
        this.userId = userId;
        this.assignment = assignment;
    }

    @PrePersist
    public void onCreate(){
        submission_date=LocalDateTime.now();
        submission_updated=LocalDateTime.now();
    }
//    @PreUpdate
//    public void onUpdate(){
//        submission_date=LocalDateTime.now();
//        submission_updated=LocalDateTime.now();
//        attempt+=1;
//    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSubmission_url() {
        return submission_url;
    }

    public void setSubmission_url(String submission_url) {
        this.submission_url = submission_url;
    }

    public LocalDateTime getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(LocalDateTime submission_date) {
        this.submission_date = submission_date;
    }

    public LocalDateTime getSubmission_updated() {
        return submission_updated;
    }

    public void setSubmission_updated(LocalDateTime submission_updated) {
        this.submission_updated = submission_updated;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
}
