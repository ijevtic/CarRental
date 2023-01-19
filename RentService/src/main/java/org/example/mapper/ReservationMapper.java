package org.example.mapper;

import org.example.domain.Reservation;
import org.example.domain.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public Reservation createReservation(Long clientId, Vehicle vehicle, Integer startTime, Integer endTime) {
        Reservation reservation = new Reservation();
        reservation.setClientId(clientId);
        reservation.setVehicle(vehicle);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        return reservation;
    }
}
