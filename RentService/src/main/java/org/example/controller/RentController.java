package org.example.controller;

import org.example.dto.*;
import org.example.service.CarService;
import org.example.util.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rent")
public class RentController {
    CarService carService;

    public RentController(CarService carService) {
        this.carService = carService;
    }

    public ResponseEntity<ServiceResponse<Boolean>> addCompany(@RequestBody CreateCompanyDto createCompanyDto){
        ServiceResponse<Boolean> response = carService.addCompany(createCompanyDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    public ResponseEntity<ServiceResponse<Boolean>> editCompany(@RequestBody EditCompanyDto editCompanyDto){
        ServiceResponse<Boolean> response = carService.editCompany(editCompanyDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    public ResponseEntity<ServiceResponse<Boolean>> addVehicle(@RequestBody AddVehicleDto addVehicleDto){
        ServiceResponse<Boolean> response = carService.addVehicle(addVehicleDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    public ResponseEntity<ServiceResponse<Boolean>> removeVehicle(@RequestBody RemoveVehicleDto removeVehicleDto){
        ServiceResponse<Boolean> response = carService.removeVehicle(removeVehicleDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    public ResponseEntity<ServiceResponse<List<GetVehicleDto>>> getAllAvailableVehicles(@RequestBody FindVehiclesDto findVehiclesDto){
        ServiceResponse<List<GetVehicleDto>> response = carService.getAvailableVehiclesDto(findVehiclesDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

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
    // Potrebno je napraviti i listu firmi za iznajmljivanje vozila,  sortiranu opadajuće po prosečnoj oceni.
    // Nisam sigurna sta su hteli sa tim. Kapiram da se odnosi na front? idk
}
