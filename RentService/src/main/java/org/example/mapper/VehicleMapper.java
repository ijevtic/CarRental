package org.example.mapper;

import org.example.domain.Vehicle;
import org.example.dto.AddVehicleDto;
import org.example.dto.VehicleDto;
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


}
