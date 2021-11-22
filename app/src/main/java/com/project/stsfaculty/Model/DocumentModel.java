package com.project.stsfaculty.Model;

public class
DocumentModel {
    public String name, email, enrollment, documentType, url, date;

    public DocumentModel() {
    }

    public DocumentModel(String name, String email, String enrollment, String documentType, String url, String date) {
        this.name = name;
        this.email = email;
        this.enrollment = enrollment;
        this.documentType = documentType;
        this.url = url;
        this.date = date;
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

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
