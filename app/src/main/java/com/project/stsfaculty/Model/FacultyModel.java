package com.project.stsfaculty.Model;

public class FacultyModel {
    String branch, name, email, password;

    public FacultyModel(String branch, String name, String email, String password) {
        this.branch = branch;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public FacultyModel(){}

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
