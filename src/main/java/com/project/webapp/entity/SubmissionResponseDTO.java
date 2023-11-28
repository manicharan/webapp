package com.project.webapp.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class SubmissionResponseDTO {
    private UUID id;
    private UUID assignment_id;
    private String submission_url;
    private LocalDateTime submission_date;
    private LocalDateTime submission_updated;

    public SubmissionResponseDTO() {
    }

    public SubmissionResponseDTO(UUID id, UUID assignment_id, String submission_url, LocalDateTime submission_date, LocalDateTime submission_updated) {
        this.id = id;
        this.assignment_id = assignment_id;
        this.submission_url = submission_url;
        this.submission_date = submission_date;
        this.submission_updated = submission_updated;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(UUID assignment_id) {
        this.assignment_id = assignment_id;
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
}
