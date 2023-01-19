package org.example.service;

import org.example.dto.*;
import org.example.util.ServiceResponse;

import java.util.List;

public interface RentService {
    ServiceResponse<Boolean> addCompany(CreateCompanyDto createCompanyDto);
    ServiceResponse<Long> getCompanyId(String companyName);
    ServiceResponse<Boolean> editCompanyDesc(String jwt, EditCompanyDto editCompanyDto);
    ServiceResponse<Boolean> addModel(String jwt, AddModelDto addModelDto);
    ServiceResponse<Boolean> changeModel(String jwt, EditModelDto addModelDto);
    ServiceResponse<Boolean> addVehicle(String jwt, AddVehicleDto addVehicleDto);
    ServiceResponse<Boolean> changeVehicle(String jwt, ChangeVehicleDto changeVehicleDto);
    ServiceResponse<Boolean> removeVehicle(RemoveVehicleDto removeVehicleDto);
    ServiceResponse<List<GetVehicleDto>> getAvailableVehiclesDto(FindVehiclesDto findVehiclesDto);
}
