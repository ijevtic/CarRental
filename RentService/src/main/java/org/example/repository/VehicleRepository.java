package org.example.repository;

import org.example.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository  extends JpaRepository<Vehicle, Long> {

}
