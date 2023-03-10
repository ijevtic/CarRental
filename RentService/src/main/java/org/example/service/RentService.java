package org.example.service;

import org.example.dto.*;
import org.example.dto.Reservation.AddReservationDto;
import org.example.dto.Reservation.RemoveReservationDto;
import org.example.dto.Reservation.ReservationDtoFull;
import org.example.dto.Vehicle.*;
import org.example.util.ServiceResponse;

import java.util.List;

public interface RentService {
    ServiceResponse<Boolean> addCompany(CreateCompanyDto createCompanyDto);
    ServiceResponse<Long> getCompanyId(String companyName);
    ServiceResponse<EditCompanyDto> getCompanyInfo(Long companyId);
    ServiceResponse<List<LocationDto>> getLocations();
    ServiceResponse<List<EditCompanyDto>> getCompanies();
    ServiceResponse<Boolean> editCompanyDesc(String jwt, EditCompanyDto editCompanyDto);
    ServiceResponse<Boolean> addModel(String jwt, AddModelDto addModelDto);
    ServiceResponse<List<VehicleDtoFull>> getVehicles(String jwt);
    ServiceResponse<List<EditModelDto>> getCompanyModels(String jwt);
    ServiceResponse<Boolean> changeModel(String jwt, EditModelDto addModelDto);
    ServiceResponse<Boolean> addVehicle(String jwt, AddVehicleDto addVehicleDto);
    ServiceResponse<Boolean> changeVehicle(String jwt, ChangeVehicleDto changeVehicleDto);
    ServiceResponse<Boolean> removeVehicle(RemoveVehicleDto removeVehicleDto);
    ServiceResponse<Boolean> addReservation(String jwt, AddReservationDto addReservationDto);
    ServiceResponse<Boolean> removeReservation(String jwt, RemoveReservationDto addReservationDto);
    ServiceResponse<List<FilterInterval>> filterVehicles(VehicleFilter vehicleFilter);

    ServiceResponse<List<ReservationDtoFull>> getUserReservations(String jwt);
    ServiceResponse<List<ReservationDtoFull>> getCompanyReservations(String jwt);

    ServiceResponse<List<GetVehicleDto>> getAvailableVehiclesDto(FindVehiclesDto findVehiclesDto);
    ServiceResponse<Boolean> addReview(String jwt, ReviewDto reviewDto);
}
