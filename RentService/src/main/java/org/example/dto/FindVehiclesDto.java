package org.example.dto;

import java.util.Date;

public class FindVehiclesDto {
    //pretraga dostupnih vozila za odredjeni period u odredejnom gradu
    private Long startDate;
    private Long endDate;
    private String city;

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
