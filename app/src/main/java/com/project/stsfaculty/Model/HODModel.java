package com.project.stsfaculty.Model;

public class HODModel {
    String branch, email, name, password;

    public HODModel(){}

    public HODModel(String branch, String email, String name, String password) {
        this.branch = branch;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
