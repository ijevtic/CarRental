package org.example.domain;

import javax.persistence.*;

@Entity
//@Table(indexes = {@Index(columnList = "companyName", unique = true)})
public class Vehicle {
    @Id
    private Long id;

    @ManyToOne(optional = false)
    private Company company;
    @ManyToOne(optional = false)
    private CarModel carModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }
}
