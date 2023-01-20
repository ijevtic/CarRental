package org.example.dto;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

public class MailDto {
    @Email
    private String clientEmail;
    private String managerEmail;
    private String clientSubject;
    private String managerSubject;
    private String clientMessage;
    private String managerMessage;

    public MailDto() {
    }

    public MailDto(String clientEmail, String managerEmail, String clientSubject, String managerSubject, String clientMessage, String managerMessage) {
        this.clientEmail = clientEmail;
        this.managerEmail = managerEmail;
        this.clientSubject = clientSubject;
        this.managerSubject = managerSubject;
        this.clientMessage = clientMessage;
        this.managerMessage = managerMessage;
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

    public String getClientSubject() {
        return clientSubject;
    }

    public void setClientSubject(String clientSubject) {
        this.clientSubject = clientSubject;
    }

    public String getManagerSubject() {
        return managerSubject;
    }

    public void setManagerSubject(String managerSubject) {
        this.managerSubject = managerSubject;
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    public String getManagerMessage() {
        return managerMessage;
    }

    public void setManagerMessage(String managerMessage) {
        this.managerMessage = managerMessage;
    }
}
