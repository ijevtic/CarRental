package org.example.dto;

public class EditModelDto {

    private Long id;
    private String modelName;
    private String carType;
    private Integer price;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String carModel) {
        this.modelName = carModel;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
