package org.example.repository;

import org.example.domain.Company;
import org.example.domain.Reservation;
import org.example.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByVehicle(Vehicle vehicle);

    Optional<Reservation> findReservationById(Long id);

    List<Reservation> findAllByClientId(Long clientId);

    @Query("select r from Reservation r join Vehicle v on r.vehicle = v join CarModel m on v.carModel = m where m.company = :company")
    List<Reservation> findAllByCompany(Company company);
}
