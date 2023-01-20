package org.example.dto.pop;

import org.example.dto.ANotification;

public class PopActivateAccount extends ANotification {
    private String email;
    private String username;
    private String token;

    public PopActivateAccount() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}