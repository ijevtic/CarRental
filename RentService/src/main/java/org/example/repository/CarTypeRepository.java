package org.example.repository;

import org.example.domain.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarTypeRepository extends JpaRepository<CarType, Long> {
    Optional<CarType> findCarTypeByTypeName(String typeName);

    Optional<CarType> getReferenceById(Long id);
}
