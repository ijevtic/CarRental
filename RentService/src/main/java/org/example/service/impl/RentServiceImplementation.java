package org.example.service.impl;

import org.example.domain.*;
import org.example.dto.*;
import org.example.mapper.CompanyMapper;
import org.example.mapper.ModelMapper;
import org.example.mapper.VehicleMapper;
import org.example.repository.*;
import org.example.security.service.TokenService;
import org.example.service.RentService;
import org.example.util.ServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class RentServiceImplementation implements RentService {

    private CompanyRepository companyRepository;
    private ModelRepository modelRepository;
    private CarTypeRepository carTypeRepository;
    private VehicleRepository vehicleRepository;
    private LocationRepository locationRepository;
    private CompanyMapper companyMapper;
    private ModelMapper modelMapper;
    private VehicleMapper vehicleMapper;
    private TokenService tokenService;

    public RentServiceImplementation(CompanyRepository companyRepository, ModelRepository modelRepository,
                                     CarTypeRepository carTypeRepository, VehicleRepository vehicleRepository,
                                     LocationRepository locationRepository, CompanyMapper companyMapper,
                                     ModelMapper modelMapper, VehicleMapper vehicleMapper, TokenService tokenService) {
        this.companyRepository = companyRepository;
        this.modelRepository = modelRepository;
        this.carTypeRepository = carTypeRepository;
        this.vehicleRepository = vehicleRepository;
        this.locationRepository = locationRepository;
        this.companyMapper = companyMapper;
        this.modelMapper = modelMapper;
        this.vehicleMapper = vehicleMapper;
        this.tokenService = tokenService;
    }

    public CarType getCarType(String carType) {
        return carTypeRepository.getReferenceById(CarType.typeMap.get(carType))
                .orElse(null);
    }

    @Override
    public ServiceResponse<Boolean> addCompany(CreateCompanyDto createCompanyDto) {
        EditCompanyDto company = companyRepository.findCompanyByCompanyName(createCompanyDto.getCompanyName())
                .map(companyMapper::companyToEditCompanyDto).orElse(null);
        if (company != null) {
            return new ServiceResponse<>(false, "Company with that name already exists", 400);
        }
        Company newCompany = companyMapper.createCompanyDtoToCompany(createCompanyDto);
        companyRepository.save(newCompany);
        return new ServiceResponse<>(true, "Company created", 201);
    }

    @Override
    public ServiceResponse<Long> getCompanyId(String companyName) {

        EditCompanyDto company = companyRepository.findCompanyByCompanyName(companyName)
                .map(companyMapper::companyToEditCompanyDto).orElse(null);
        if(company == null) {
            return new ServiceResponse<>(null, "Company with that name does not exist", 400);
        }
        System.out.println(company.getId());
        return new ServiceResponse<>(company.getId(), "Company found", 200);
    }

    @Override
    public ServiceResponse<Boolean> editCompanyDesc(String jwt, EditCompanyDto editCompanyDto) {
        Long companyId = tokenService.getCompanyId(jwt);
        EditCompanyDto oldCompanyDto = companyRepository.findCompanyById(companyId).map(companyMapper::companyToEditCompanyDto).orElse(null);
        if (oldCompanyDto == null) {
            return new ServiceResponse<>(false, "Company not found", 404);
        }
        Company updateCompany = companyMapper.editCompanyDtoToCompany(oldCompanyDto, editCompanyDto);
        companyRepository.updateCompanyById(companyId, updateCompany.getCompanyName(), updateCompany.getDescription());
        return new ServiceResponse<>(true, "Company updated", 200);
    }

    @Override
    public ServiceResponse<Boolean> addModel(String jwt, AddModelDto addModelDto) { //model, type, price
        Long companyId = tokenService.getCompanyId(jwt);
        EditCompanyDto companyDto = companyRepository.findCompanyById(companyId).map(companyMapper::companyToEditCompanyDto).orElse(null);
        if (companyDto == null) {
            return new ServiceResponse<>(false, "Company not found", 404);
        }
        CarModel carModel = modelRepository.findCarModelByModelNameAndCarType(addModelDto.getModelName(), getCarType(addModelDto.getCarType()))
                .orElse(null);
        if (carModel != null) {
            return new ServiceResponse<>(false, "Pair <Model, Type> already exists!", 400);
        }
        carModel = modelMapper.carModelDtoToCarModel(addModelDto, companyId);
        modelRepository.save(carModel);
        return new ServiceResponse<>(true, "Car model added", 201);
    }

    @Override
    public ServiceResponse<Boolean> changeModel(String jwt, EditModelDto editModelDto) { //model, type, price
        Long companyId = tokenService.getCompanyId(jwt);
        Company company = companyRepository.findCompanyById(companyId).orElse(null);
        if (company == null) {
            return new ServiceResponse<>(false, "Company not found", 404);
        }
        EditModelDto oldCarModel = modelRepository.findCarModelById(editModelDto.getId())
                .map(modelMapper::carModelToCarModelDto).orElse(null);
        if (oldCarModel == null) {
            return new ServiceResponse<>(false, "Model id doesn't exist!", 400);
        }
        CarModel newCarModel = modelMapper.editModelDtoToModel(oldCarModel, editModelDto, companyId);
        System.out.println(newCarModel);
        modelRepository.updateCarModelById(editModelDto.getId(), newCarModel.getModelName(), newCarModel.getPrice(), newCarModel.getCarType());
        return new ServiceResponse<>(true, "Car model modified", 200);
    }

    @Override
    public ServiceResponse<Boolean> addVehicle(String jwt, AddVehicleDto addVehicleDto) {
        Long companyId = tokenService.getCompanyId(jwt);
        Company company = companyRepository.findCompanyById(companyId).orElse(null);
        if (company == null) {
            return new ServiceResponse<>(false, "Company not found", 404);
        }
        CarModel carModel = modelRepository.findCarModelById(addVehicleDto.getModelId()).orElse(null);
        if(!carModel.getCompany().getId().equals(companyId)) {
            return new ServiceResponse<>(false, "Model doesn't belong to your company", 400);
        }
        Vehicle vehicle = vehicleMapper.createVehicle(addVehicleDto);
        vehicleRepository.save(vehicle);
        return new ServiceResponse<>(true, "Vehicle created!", 201);
    }

    @Override
    public ServiceResponse<Boolean> changeVehicle(String jwt, ChangeVehicleDto changeVehicleDto) {
        Long companyId = tokenService.getCompanyId(jwt);
        Company company = companyRepository.findCompanyById(companyId).orElse(null);
        if (company == null) {
            return new ServiceResponse<>(false, "Company not found", 404);
        }
        VehicleDto vehicleDto = vehicleRepository.findVehicleById(changeVehicleDto.getId()).map(vehicleMapper::vehicleToVehicleDto).orElse(null);
        if(vehicleDto == null) {
            return new ServiceResponse<>(false, "Vehicle not found", 404);
        }
        Vehicle v = vehicleRepository.checkVehicle(changeVehicleDto.getId(), modelRepository.getReferenceById(vehicleDto.getModelId()).orElse(null),
                companyRepository.getReferenceById(companyId).orElse(null)).orElse(null);
        if(v == null) {
            return new ServiceResponse<>(false, "Vehicle doesn't belong to your company", 400);
        }
        Location location = v.getLocation();
        if(changeVehicleDto.getLocationId() != null)
            location = locationRepository.getReferenceById(changeVehicleDto.getLocationId()).orElse(null);
        vehicleRepository.updateVehicle(changeVehicleDto.getId(), location);
        return new ServiceResponse<>(true, "Vehicle updated", 200);
    }

    @Override
    public ServiceResponse<Boolean> removeVehicle(RemoveVehicleDto removeVehicleDto) {
        return null;
    }

    @Override
    public ServiceResponse<List<GetVehicleDto>> getAvailableVehiclesDto(FindVehiclesDto findVehiclesDto) {
        return null;
    }
}
