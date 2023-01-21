package org.example.repository;

import org.example.domain.CarModel;
import org.example.domain.CarType;
import org.example.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<CarModel, Long> {
//    Optional<CarModel> findModelByModelName(String name);

    Optional<CarModel> findCarModelByModelNameAndCarType(String modelName, CarType carType);

    Optional<CarModel> getReferenceById(Long id);

    Optional<CarModel> findCarModelById(Long id);

    List<CarModel> findCarModelsByCompany(Company company);


    @Modifying
    @Query("update CarModel m set m.modelName = :modelName, m.price = :price, m.carType = :carType where m.id = :id")
    void updateCarModelById(Long id, String modelName, Integer price, CarType carType);

}
