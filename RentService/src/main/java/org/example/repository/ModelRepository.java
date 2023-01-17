package org.example.repository;

import org.example.domain.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelRepository extends JpaRepository<CarModel, Long> {
    Optional<CarModel> findModelByModelName(String name);

    Optional<CarModel> getReferenceById(Long id);
}
