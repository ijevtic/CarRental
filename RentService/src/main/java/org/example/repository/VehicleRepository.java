package org.example.repository;

import org.example.domain.CarModel;
import org.example.domain.Company;
import org.example.domain.Location;
import org.example.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface VehicleRepository  extends JpaRepository<Vehicle, Long> {
    @Query("select v from Vehicle v join CarModel m on v.carModel = :model where m.company = :company and v.id = :id")
    Optional<Vehicle> checkVehicle(Long id, CarModel model, Company company);

    Optional<Vehicle> findVehicleById(Long id);

    @Modifying
    @Query("update Vehicle v set v.location = :location where v.id = :id")
    void updateVehicle(Long id, Location location);

//    @Query("SELECT v from Vehicle v left outer join Reservation r on v.id = r.vehicle.id WHERE r.startTime < :startTime and r.endTime < :startTime or r.startTime < :endTime and r.endTime > :endTime")
    @Query("SELECT v from Vehicle v")
    List<Vehicle> findAll();
}
