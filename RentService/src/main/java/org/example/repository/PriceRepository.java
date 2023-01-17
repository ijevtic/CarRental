package org.example.repository;

import org.example.domain.CarPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<CarPrice, Long> {
    Optional<Object> findPriceByCarModelIdAndCarTypeId(Long modelId, Long carTypeId);

//    Optional<Object> getReferenceById(Long id);
}
