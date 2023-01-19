package org.example.dto.Reservation;

import javax.validation.constraints.NotBlank;

public class RemoveReservationDto {
    @NotBlank
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
