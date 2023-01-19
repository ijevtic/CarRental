package org.example.dto.Vehicle;

import javax.validation.constraints.NotBlank;

public class VehicleFilter {
    private Long companyId;
    private Long carTypeId;
    private Long locationId;
//    @NotBlank
    private Integer startTime;
//    @NotBlank
    private Integer endTime;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(Long carTypeId) {
        this.carTypeId = carTypeId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }
}
