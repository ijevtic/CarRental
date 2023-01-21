package org.example.mapper;

import org.example.domain.CarModel;
import org.example.domain.Location;
import org.example.domain.Vehicle;
import org.example.dto.AddVehicleDto;
import org.example.dto.Vehicle.FilterInterval;
import org.example.dto.Vehicle.VehicleDto;
import org.example.dto.Vehicle.VehicleDtoFull;
import org.example.repository.LocationRepository;
import org.example.repository.ModelRepository;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    private ModelRepository modelRepository;
    private LocationRepository locationRepository;

    public VehicleMapper(ModelRepository modelRepository, LocationRepository locationRepository) {
        this.modelRepository = modelRepository;
        this.locationRepository = locationRepository;
    }

    public Vehicle createVehicle(AddVehicleDto vehicleDto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setCarModel(modelRepository.getReferenceById(vehicleDto.getModelId()).orElse(null));
        vehicle.setLocation(locationRepository.getReferenceById(vehicleDto.getLocationId()).orElse(null));
        return vehicle;
    }

    public VehicleDto vehicleToVehicleDto(Vehicle vehicle) {
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setId(vehicle.getId());
        vehicleDto.setModelId(vehicle.getCarModel().getId());
        vehicleDto.setLocationId(vehicle.getLocation().getId());
        return vehicleDto;
    }

    public VehicleDtoFull vehicleToVehicleDtoFull(Vehicle vehicle) {
        VehicleDtoFull vehicleDto = new VehicleDtoFull();
        vehicleDto.setId(vehicle.getId());
        vehicleDto.setModelId(vehicle.getCarModel().getId());
        vehicleDto.setLocationId(vehicle.getLocation().getId());
        vehicleDto.setTypeName(vehicle.getCarModel().getCarType().getTypeName());
        vehicleDto.setCompanyName(vehicle.getCarModel().getCompany().getCompanyName());
        vehicleDto.setPrice(vehicle.getCarModel().getPrice());
        vehicleDto.setModelName(vehicle.getCarModel().getModelName());
        return vehicleDto;
    }

    public FilterInterval vehicleDtoToFilterInterval(VehicleDtoFull vehicleDto) {
        FilterInterval filterInterval = new FilterInterval();
        filterInterval.setLocationId(vehicleDto.getLocationId());
        filterInterval.setModelId(vehicleDto.getModelId());
        filterInterval.setCompanyName(vehicleDto.getCompanyName());
        filterInterval.setTypeName(vehicleDto.getTypeName());
        filterInterval.setPrice(vehicleDto.getPrice());
        Location location = locationRepository.getLocationById(vehicleDto.getLocationId()).orElse(null);
        if(location != null) {
            filterInterval.setLocationName(location.getCity());
        }
        CarModel carModel = modelRepository.findCarModelById(vehicleDto.getModelId()).orElse(null);
        if(carModel != null) {
            filterInterval.setModelName(carModel.getModelName());
        }
        return filterInterval;
    }


}
