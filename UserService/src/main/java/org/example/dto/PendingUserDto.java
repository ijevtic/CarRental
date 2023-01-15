package org.example.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class PendingUserDto {
    @Email
    private String email;
    @NotBlank
    private String verificationCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
