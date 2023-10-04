package com.project.webapp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String first_name;
    private String last_name;
    private String password;
    private String email;
    private LocalDateTime account_created;
    private LocalDateTime account_updated;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "user")
    private List<Assignment> assignmentList;

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getAccount_created() {
        return account_created;
    }

    public void setAccount_created(LocalDateTime account_created) {
        this.account_created = account_created;
    }

    public LocalDateTime getAccount_updated() {
        return account_updated;
    }

    public void setAccount_updated(LocalDateTime account_updated) {
        this.account_updated = account_updated;
    }

    public User(long id, String first_name, String password, String email, LocalDateTime account_created, LocalDateTime account_updated) {
        this.id = id;
        this.first_name = first_name;
        this.password = password;
        this.email = email;
        this.account_created = account_created;
        this.account_updated = account_updated;
    }

    public User() {
    }

    @PrePersist
    public void onCreate(){
        account_created=LocalDateTime.now();
        account_updated=LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate(){
        account_updated=LocalDateTime.now();
    }
}
