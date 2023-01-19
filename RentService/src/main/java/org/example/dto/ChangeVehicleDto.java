package org.example.dto;

import javax.validation.constraints.NotBlank;

public class ChangeVehicleDto {
    private Long id;
    @NotBlank
    private Long locationId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}