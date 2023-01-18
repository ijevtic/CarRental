package org.example.service;

import org.example.dto.*;
import org.example.util.ServiceResponse;

import java.util.List;

public interface CarService {
    ServiceResponse<Boolean> addCompany(CreateCompanyDto createCompanyDto);
    ServiceResponse<Boolean> editCompany(EditCompanyDto editCompanyDto);
    ServiceResponse<Boolean> addVehicle(AddVehicleDto addVehicleDto);
    ServiceResponse<Boolean> removeVehicle(RemoveVehicleDto removeVehicleDto);
    ServiceResponse<List<GetVehicleDto>> getAvailableVehiclesDto(FindVehiclesDto findVehiclesDto);
}
