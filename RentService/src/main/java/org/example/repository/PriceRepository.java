package org.example.repository;

import org.example.domain.CarPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PriceRepository extends JpaRepository<CarPrice, Long> {
    Optional<Object> findPriceByCarModelIdAndCarTypeId(Long modelId, Long carTypeId);

//    Optional<Object> getReferenceById(Long id);
}
