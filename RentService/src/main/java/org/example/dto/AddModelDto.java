package org.example.dto;

public class AddModelDto {
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
}
