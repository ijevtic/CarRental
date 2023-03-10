package org.example.controller;

import org.example.dto.*;
import org.example.dto.Reservation.AddReservationDto;
import org.example.dto.Reservation.RemoveReservationDto;
import org.example.dto.Reservation.ReservationDtoFull;
import org.example.dto.Vehicle.*;
import org.example.security.CheckSecurity;
import org.example.service.RentService;
import org.example.util.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/rent")
public class RentController {
    RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    //radi
    @PostMapping("/addCompany")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<ServiceResponse<Boolean>> addCompany(@RequestHeader("Authorization") String authorization,
                                                               @RequestBody CreateCompanyDto createCompanyDto){
        ServiceResponse<Boolean> response = rentService.addCompany(createCompanyDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    //radi
    @GetMapping("/getCompany/{name}")
    @CheckSecurity(roles = {"ADMIN", "MANAGER", "USER"})
    public ResponseEntity<ServiceResponse<Long>> getCompany(@RequestHeader("Authorization") String authorization,
                                                               @PathVariable("name") String name){
        ServiceResponse<Long> response = rentService.getCompanyId(name);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/getCompanyInfo/{id}")
    @CheckSecurity(roles = {"ADMIN", "MANAGER", "USER"})
    public ResponseEntity<ServiceResponse<EditCompanyDto>> getCompanyInfo(@RequestHeader("Authorization") String authorization,
                                                            @PathVariable("id") Long companyId){
        ServiceResponse<EditCompanyDto> response = rentService.getCompanyInfo(companyId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/getLocations")
    public ResponseEntity<ServiceResponse<List<LocationDto>>> getLocations(){
        ServiceResponse<List<LocationDto>> response = rentService.getLocations();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/getCompanies")
    public ResponseEntity<ServiceResponse<List<EditCompanyDto>>> getCompanies(){
        ServiceResponse<List<EditCompanyDto>> response = rentService.getCompanies();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }


    //radi
    @PostMapping("/editCompanyDesc")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<ServiceResponse<Boolean>> editCompanyDesc(@RequestHeader("Authorization") String authorization,
                                                                    @RequestBody EditCompanyDto editCompanyDto){

        System.out.println(authorization);
        ServiceResponse<Boolean> response = rentService.editCompanyDesc(authorization, editCompanyDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/addModel")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<ServiceResponse<Boolean>> addModel(@RequestHeader("Authorization") String authorization,
                                                                    @RequestBody AddModelDto addModelDto){
        ServiceResponse<Boolean> response = rentService.addModel(authorization, addModelDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/getVehicles")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<ServiceResponse<List<VehicleDtoFull>>> getVehicles(@RequestHeader("Authorization") String authorization){
        ServiceResponse<List<VehicleDtoFull>> response = rentService.getVehicles(authorization);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/getCompanyModels")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<ServiceResponse<List<EditModelDto>>> getCompanyModels(@RequestHeader("Authorization") String authorization){
        ServiceResponse<List<EditModelDto>> response = rentService.getCompanyModels(authorization);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/changeModel")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<ServiceResponse<Boolean>> changeModel(@RequestHeader("Authorization") String authorization,
                                                             @RequestBody EditModelDto editModelDto){
        ServiceResponse<Boolean> response = rentService.changeModel(authorization, editModelDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    //TODO dodavanje vise vozila odjednom
    @PostMapping("/addVehicle")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<ServiceResponse<Boolean>> addVehicle(@RequestHeader("Authorization") String authorization,
                                                                @RequestBody AddVehicleDto addVehicleDto){
        ServiceResponse<Boolean> response = rentService.addVehicle(authorization, addVehicleDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/changeVehicle")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<ServiceResponse<Boolean>> changeVehicle(@RequestHeader("Authorization") String authorization,
                                                               @RequestBody ChangeVehicleDto changeVehicleDto){
        ServiceResponse<Boolean> response = rentService.changeVehicle(authorization, changeVehicleDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/addReservation")
    @CheckSecurity(roles = {"ADMIN", "USER"})
    public ResponseEntity<ServiceResponse<Boolean>> addReservation(@RequestHeader("Authorization") String authorization,
                                                                   @RequestBody AddReservationDto addReservationDto){
        ServiceResponse<Boolean> response = rentService.addReservation(authorization, addReservationDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/removeReservation")
    @CheckSecurity(roles = {"ADMIN", "USER", "MANAGER"})
    public ResponseEntity<ServiceResponse<Boolean>> removeReservation(@RequestHeader("Authorization") String authorization,
                                                                         @RequestBody RemoveReservationDto removeReservationDto){
        ServiceResponse<Boolean> response = rentService.removeReservation(authorization, removeReservationDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/filterVehicles")
    @CheckSecurity(roles = {"ADMIN", "USER", "MANAGER"})
    public ResponseEntity<ServiceResponse<List<FilterInterval>>> filterVehicles(@RequestHeader("Authorization") String authorization,
                                                                                @RequestBody @Valid VehicleFilter vehicleFilter){
        ServiceResponse<List<FilterInterval>> response = rentService.filterVehicles(vehicleFilter);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/getUserReservations")
    @CheckSecurity(roles = {"ADMIN", "USER"})
    public ResponseEntity<ServiceResponse<List<ReservationDtoFull>>> getUserReservations(@RequestHeader("Authorization") String authorization){
        ServiceResponse<List<ReservationDtoFull>> response = rentService.getUserReservations(authorization);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/getCompanyReservations")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<ServiceResponse<List<ReservationDtoFull>>> getCompanyReservations(@RequestHeader("Authorization") String authorization){
        ServiceResponse<List<ReservationDtoFull>> response = rentService.getCompanyReservations(authorization);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/addReview")
    @CheckSecurity(roles = {"USER"})
    public ResponseEntity<ServiceResponse<Boolean>> addReview(@RequestHeader("Authorization") String authorization,
                                                              @RequestBody ReviewDto reviewDto){
        ServiceResponse<Boolean> response = rentService.addReview(authorization, reviewDto);
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getStatusCode()));
    }

//    public ResponseEntity<ServiceResponse<Boolean>> removeVehicle(@RequestBody RemoveVehicleDto removeVehicleDto){
//        ServiceResponse<Boolean> response = rentService.removeVehicle(removeVehicleDto);
//        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
//    }
//
//    public ResponseEntity<ServiceResponse<List<GetVehicleDto>>> getAllAvailableVehicles(@RequestBody FindVehiclesDto findVehiclesDto){
//        ServiceResponse<List<GetVehicleDto>> response = rentService.getAvailableVehiclesDto(findVehiclesDto);
//        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
//    }

    //SEND RESERVATION CONFIRMATION
    // asinhrono (message broker) poslati poruku notif. servisu da je korisnik rezervisao vozilo

    //CALCULATE RESERVATION PRICE
    //Iz user servisa dohvatiti podatke o korisniku koji je rezervisao vozilo (Rank i Popust)
    //sinhrono preko hhtp poziva

    //PONOVLJENI ZAHTEV
    //ako je servis nedostupan - retry pattern

    //OBAVESTAVANJE SERVISA O REZERVACIJI
    //da se rezervisani auto setuje kao nedostupan za odabrani period - da se azurira br slobodnih modela tipova i ukupan br
    //da se na korisnickom nalogu promeni ukupno trajanje rezervacija

    //OTKAZIVANJE REZERVACIJE
    //sve isto kao ovi iznad samo obrnuto

    //OSTAVLJANJE RECENZIJE
    //Listanje po gradu i firmi
    //brisanje i editovanje postojecih
    //
    // Potrebno je napraviti i listu firmi za iznajmljivanje vozila,  sortiranu opadaju??e po prose??noj oceni.
    // Nisam sigurna sta su hteli sa tim. Kapiram da se odnosi na front? idk
}
