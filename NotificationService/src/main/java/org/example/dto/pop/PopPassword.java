package org.example.dto.pop;

import org.example.dto.ANotification;

public class PopPassword extends ANotification {
    private String email;
    private String username;

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
}
