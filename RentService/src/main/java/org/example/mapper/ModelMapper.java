package org.example.mapper;

import org.example.domain.CarModel;
import org.example.domain.CarType;
import org.example.domain.ECarType;
import org.example.dto.AddModelDto;
import org.example.dto.EditModelDto;
import org.example.repository.CarTypeRepository;
import org.example.repository.CompanyRepository;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {
    private CarTypeRepository carTypeRepository;
    private CompanyRepository companyRepository;

    public ModelMapper(CarTypeRepository carTypeRepository, CompanyRepository companyRepository) {
        this.carTypeRepository = carTypeRepository;
        this.companyRepository = companyRepository;
    }

    public CarModel carModelDtoToCarModel(AddModelDto addModelDto, Long companyId) {
        CarModel carModel = new CarModel();
        carModel.setModelName(addModelDto.getModelName());
        carModel.setCarType(carTypeRepository.getReferenceById(CarType.typeMap.get(ECarType.valueOf(addModelDto.getCarType())))
                .orElse(null));
        carModel.setCompany(companyRepository.findCompanyById(companyId).orElse(null));
        carModel.setPrice(addModelDto.getPrice());
        return carModel;
    }

    public EditModelDto carModelToCarModelDto(CarModel carModel) {
        EditModelDto modelDto = new EditModelDto();
        modelDto.setModelName(carModel.getModelName());
        modelDto.setCarType(carModel.getCarType().toString());
        modelDto.setPrice(carModel.getPrice());
        return modelDto;
    }

    public CarModel editModelDtoToModel(EditModelDto oldAddModelDto, EditModelDto newAddModelDto, Long companyId) {
        CarModel model = new CarModel();
        if(newAddModelDto.getModelName() != null) {
            model.setModelName(newAddModelDto.getModelName());
        } else {
            model.setModelName(oldAddModelDto.getModelName());
        }
        if(newAddModelDto.getCarType() != null) {
            model.setCarType(carTypeRepository.getReferenceById(CarType.typeMap.get(ECarType.valueOf(newAddModelDto.getCarType())))
                    .orElse(null));
        } else {
            model.setCarType(carTypeRepository.getReferenceById(CarType.typeMap.get(ECarType.valueOf(oldAddModelDto.getCarType())))
                    .orElse(null));
        }
        if(newAddModelDto.getPrice() != null) {
            model.setPrice(newAddModelDto.getPrice());
        } else {
            model.setPrice(oldAddModelDto.getPrice());
        }
        model.setCompany(companyRepository.getReferenceById(companyId).orElse(null));
        return model;
    }
}
