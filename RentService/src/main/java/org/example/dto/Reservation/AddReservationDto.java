package org.example.dto.Reservation;

import javax.validation.constraints.NotBlank;

public class AddReservationDto {
    @NotBlank
    private Long carModelId;
    @NotBlank
    private Long locationId;
    @NotBlank
    private Integer startTime;
    @NotBlank
    private Integer endTime;


    public Long getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(Long carModelId) {
        this.carModelId = carModelId;
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

    @Override
    public String toString() {
        return "AddReservationDto{" +
                "carModelId=" + carModelId +
                ", locationId=" + locationId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
