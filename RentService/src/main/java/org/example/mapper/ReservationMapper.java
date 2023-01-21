package org.example.mapper;

import org.example.domain.Reservation;
import org.example.domain.Vehicle;
import org.example.dto.Reservation.ReservationDtoFull;
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

    public ReservationDtoFull reservationToReservationMapFull(Reservation reservation) {
        ReservationDtoFull res = new ReservationDtoFull();
        res.setReservationId(reservation.getId());
        res.setCompanyName(reservation.getVehicle().getCarModel().getCompany().getCompanyName());
        res.setModelName(reservation.getVehicle().getCarModel().getModelName());
        res.setTypeName(reservation.getVehicle().getCarModel().getCarType().getTypeName());
        res.setPrice(reservation.getVehicle().getCarModel().getPrice());
        res.setCity(reservation.getVehicle().getLocation().getCity());
        res.setStartTime(reservation.getStartTime());
        res.setEndTime(reservation.getEndTime());
        return res;

    }
}
