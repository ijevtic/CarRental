package org.example.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class UserCreateDto extends AUserCreateDto{

    private String passportNumber;
    private String totalRentDays;

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getTotalRentDays() {
        return totalRentDays;
    }

    public void setTotalRentDays(String totalRentDays) {
        this.totalRentDays = totalRentDays;
    }
}
