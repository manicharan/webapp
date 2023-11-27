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
    private UUID user_id;
    private int attempt;
    @ManyToOne
    @JoinColumn(name="assignment_id")
    @JsonIgnore
    private Assignment assignment;

    public Submission() {
    }

    public Submission(UUID id, String submission_url, LocalDateTime submission_date, LocalDateTime submission_updated, UUID user_id, int attempt, Assignment assignment) {
        this.id = id;
        this.submission_url = submission_url;
        this.submission_date = submission_date;
        this.submission_updated = submission_updated;
        this.user_id = user_id;
        this.attempt = attempt;
        this.assignment = assignment;
    }

    @PrePersist
    public void onCreate(){
        submission_date=LocalDateTime.now();
        submission_updated=LocalDateTime.now();
        attempt=1;
    }
    @PreUpdate
    public void onUpdate(){
        submission_date=LocalDateTime.now();
        submission_updated=LocalDateTime.now();
        attempt+=1;
    }

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
        return user_id;
    }

    public void setUserId(UUID user_id) {
        this.user_id = user_id;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
}
