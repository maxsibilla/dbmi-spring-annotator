package edu.pitt.nccih.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    @Transient
    private String passwordConfirm;

    @ManyToMany(fetch=FetchType.EAGER)
    private Set<Role> roles;

//    @Column
//    @ElementCollection (targetClass = String.class)
    private ArrayList<String> uncompletedFiles;

    private String nextFileToComplete;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @JsonIgnore
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public ArrayList<String> getUncompletedFiles() {
        return uncompletedFiles;
    }

    public void setUncompletedFiles(ArrayList<String> uncompletedFiles) {
        this.uncompletedFiles = uncompletedFiles;
    }

    public String getNextFileToComplete() {
        return nextFileToComplete;
    }

    public void setNextFileToComplete(String nextFileToComplete) {
        this.nextFileToComplete = nextFileToComplete;
    }
}