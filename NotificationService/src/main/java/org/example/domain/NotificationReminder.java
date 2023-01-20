package org.example.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NotificationReminder {
    @Id
    private Long id;
    private String clientEmail;
    private String managerEmail;
    private String clientUsername;
    private String modelName;
    private String city;
    private String companyName;
    private Integer startTime;

    public NotificationReminder() {
    }

    public NotificationReminder(Long id, String clientEmail, String managerEmail, String clientUsername, String modelName, String city, String companyName, Integer startTime) {
        this.id = id;
        this.clientEmail = clientEmail;
        this.managerEmail = managerEmail;
        this.clientUsername = clientUsername;
        this.modelName = modelName;
        this.city = city;
        this.companyName = companyName;
        this.startTime = startTime;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
