package org.example.dto.Vehicle;

import java.util.Date;

public class FindVehiclesDto {
    //pretraga dostupnih vozila za odredjeni period u odredejnom gradu
    private Integer startDate;
    private Integer endDate;
    private String city;

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
