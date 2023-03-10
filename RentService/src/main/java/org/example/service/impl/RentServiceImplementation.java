package org.example.service.impl;

import org.example.client.notificationservice.NotificationMQ;
import org.example.client.notificationservice.pop.PopCancel;
import org.example.client.notificationservice.pop.PopReservation;
import org.example.client.userservice.RentUserDto;
import org.example.domain.*;
import org.example.dto.*;
import org.example.dto.Reservation.AddReservationDto;
import org.example.dto.Reservation.RemoveReservationDto;
import org.example.dto.Reservation.ReservationDtoFull;
import org.example.dto.Vehicle.*;
import org.example.helper.MessageHelper;
import org.example.mapper.*;
import org.example.repository.*;
import org.example.security.service.TokenService;
import org.example.service.RentService;
import org.example.util.ServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class RentServiceImplementation implements RentService {

    private CompanyRepository companyRepository;
    private ModelRepository modelRepository;
    private CarTypeRepository carTypeRepository;
    private VehicleRepository vehicleRepository;
    private LocationRepository locationRepository;
    private ReservationRepository reservationRepository;
    private CompanyMapper companyMapper;
    private ModelMapper modelMapper;
    private VehicleMapper vehicleMapper;
    private ReservationMapper reservationMapper;
    private LocationMapper locationMapper;
    private TokenService tokenService;
    private RestTemplate userServiceRestTemplate;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private String notificationQueue;
    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;

    public RentServiceImplementation(CompanyRepository companyRepository, ModelRepository modelRepository,
                                     CarTypeRepository carTypeRepository, VehicleRepository vehicleRepository,
                                     LocationRepository locationRepository, CompanyMapper companyMapper,
                                     ModelMapper modelMapper, VehicleMapper vehicleMapper, TokenService tokenService,
                                     RestTemplate userServiceRestTemplate, ReservationRepository reservationRepository,
                                     ReservationMapper reservationMapper, JmsTemplate jmsTemplate,
                                     ReviewMapper reviewMapper, ReviewRepository reviewRepository,
                                     MessageHelper messageHelper, @Value("${async.notifications}") String notificationQueue,
                                     LocationMapper locationMapper) {
        this.companyRepository = companyRepository;
        this.modelRepository = modelRepository;
        this.carTypeRepository = carTypeRepository;
        this.vehicleRepository = vehicleRepository;
        this.locationRepository = locationRepository;
        this.reservationRepository = reservationRepository;
        this.companyMapper = companyMapper;
        this.modelMapper = modelMapper;
        this.vehicleMapper = vehicleMapper;
        this.tokenService = tokenService;
        this.userServiceRestTemplate = userServiceRestTemplate;
        this.reservationMapper = reservationMapper;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.notificationQueue = notificationQueue;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.locationMapper = locationMapper;
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
        return new ServiceResponse<>(company.getId(), "Company found", 200);
    }

    @Override
    public ServiceResponse<EditCompanyDto> getCompanyInfo(Long companyId) {
        EditCompanyDto company = companyRepository.findCompanyById(companyId)
                .map(companyMapper::companyToEditCompanyDto).orElse(null);
        if(company == null) {
            return new ServiceResponse<>(null, "Company with that id does not exist", 400);
        }
        return new ServiceResponse<>(company, "Company found", 200);
    }

    @Override
    public ServiceResponse<List<LocationDto>> getLocations() {
        List<LocationDto> locationDtos = locationRepository.findAll().stream().map(locationMapper::locationToLocationDto)
                .collect(Collectors.toList());
        return new ServiceResponse<>(locationDtos, "Locations found", 200);
    }

    @Override
    public ServiceResponse<List<EditCompanyDto>> getCompanies() {
        List<EditCompanyDto> companyDtos = companyRepository.findAll().stream().map(companyMapper::companyToEditCompanyDto)
                .collect(Collectors.toList());
        return new ServiceResponse<>(companyDtos, "Companies found", 200);
    }

    @Override
    public ServiceResponse<Boolean> editCompanyDesc(String jwt, EditCompanyDto editCompanyDto) {
        Pair<String, Long> userInfo = tokenService.getUserInfo(jwt);
        EditCompanyDto oldCompanyDto = companyRepository.findCompanyById(editCompanyDto.getId())
                .map(companyMapper::companyToEditCompanyDto).orElse(null);
        if(oldCompanyDto == null) {
            return new ServiceResponse<>(false, "Company with that id does not exist", 400);
        }
        if(!userInfo.getFirst().equals("ADMIN")) {
            Long companyId = tokenService.getCompanyId(jwt);
            if (!companyId.equals(editCompanyDto.getId())) {
                return new ServiceResponse<>(false, "You are not authorized to edit this company", 403);
            }
        }
        Company updateCompany = companyMapper.editCompanyDtoToCompany(oldCompanyDto, editCompanyDto);
        companyRepository.updateCompanyById(editCompanyDto.getId(), updateCompany.getCompanyName(), updateCompany.getDescription());
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
    public ServiceResponse<List<VehicleDtoFull>> getVehicles(String jwt) {
        Long companyId = tokenService.getCompanyId(jwt);
        List<VehicleDtoFull> list = vehicleRepository.findVehicleByCompany(companyRepository.getReferenceById(companyId).orElse(null)).stream()
                .map(vehicleMapper::vehicleToVehicleDtoFull).collect(Collectors.toList());
        return new ServiceResponse<>(list, "Vehicles found", 200);
    }

    @Override
    public ServiceResponse<List<EditModelDto>> getCompanyModels(String jwt) {
        Long companyId = tokenService.getCompanyId(jwt);
        List<EditModelDto> list = modelRepository.findCarModelsByCompany(companyRepository.getReferenceById(companyId).orElse(null)).stream()
                .map(modelMapper::carModelToCarModelDto).collect(Collectors.toList());
        return new ServiceResponse<>(list, "Models found", 200);
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


    //TODO start date posle end date
    @Override
    public ServiceResponse<Boolean> addReservation(String jwt, AddReservationDto input) {
        System.out.println(input);

        Long userId = tokenService.getUserId(jwt);

        ResponseEntity<ServiceResponse<RentUserDto>> response = null;
        RentUserDto client = null;
        try {
            response = userServiceRestTemplate.exchange("/findUserEmail/" + userId,
                    HttpMethod.GET, null, new ParameterizedTypeReference<ServiceResponse<RentUserDto>>() {});
            client = response.getBody().getData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServiceResponse<>(false, "user with a given id does not exist!", 404);
        }
        if(client == null) {
            return new ServiceResponse<>(false, "user with a given id does not exist!", 404);
        }
        List<Vehicle> vehicles = vehicleRepository.findAll().stream().
                filter(v -> (input.getCarModelId() == null || v.getCarModel().getId().equals(input.getCarModelId()))
                        && (input.getLocationId() == null || v.getLocation().getId().equals(input.getLocationId())))
                .collect(Collectors.toList());

        if(vehicles.isEmpty()) {
            return new ServiceResponse<>(false, "No vehicles available in given time range", 404);
        }
        Reservation reservation = null;
        for(Vehicle v : vehicles) {
            System.out.println(v.getId());
            List<Reservation> reservations = reservationRepository.findAllByVehicle(v);
            if(reservations.isEmpty()) {
                reservation = reservationMapper.createReservation(userId, v, input.getStartTime(), input.getEndTime());
                break;
            }
            boolean ok = true;
            for(Reservation r : reservations) {

                if(!(r.getStartTime() < input.getStartTime() && r.getEndTime() < input.getStartTime())
                        && !(r.getStartTime() > input.getEndTime() && r.getEndTime() > input.getEndTime())){
                    ok = false;
                    break;
                }
            }
            if(ok) {
                reservation = reservationMapper.createReservation(userId, v, input.getStartTime(), input.getEndTime());
                break;
            }
        }
        if(reservation == null) {
            return new ServiceResponse<>(false, "No vehicles available in given time range", 404);
        }
        RentUserDto manager = getManager(reservation.getVehicle().getCarModel().getCompany().getId());
        Long reservationId = reservationRepository.save(reservation).getId();
        NotificationMQ<PopReservation> q = new NotificationMQ<>();
        q.setType("RESERVATION");
        q.setData(new PopReservation(client.getEmail(), manager.getEmail(), client.getUsername(),
                reservation.getVehicle().getCarModel().getModelName(), reservation.getVehicle().getLocation().getCity(),
                reservation.getVehicle().getCarModel().getCompany().getId(), reservation.getVehicle().getCarModel().getCompany().getCompanyName(),
                input.getStartTime(), input.getEndTime(), reservationId));
        jmsTemplate.convertAndSend(notificationQueue, messageHelper.createTextMessage(q));
        updateUserRentDays(userId, input.getEndTime() - input.getStartTime());
        return new ServiceResponse<>(true, "Reservation created", 201);

    }

    @Override
    public ServiceResponse<Boolean> removeReservation(String jwt, RemoveReservationDto removeReservationDto) {
        Pair<String, Long> userInfo = tokenService.getUserInfo(jwt);
        Reservation reservation = reservationRepository.findReservationById(removeReservationDto.getId()).orElse(null);
        if(reservation == null) {
            return new ServiceResponse<>(false, "Reservation not found", 404);
        }
        if((userInfo.getFirst().equals("MANAGER") && !reservation.getVehicle().getCarModel().getCompany().getId().equals(userInfo.getSecond())) ||
                (userInfo.getFirst().equals("USER") && !reservation.getClientId().equals(userInfo.getSecond()))) {
            return new ServiceResponse<>(false, "You can't cancel this reservation", 400);
        }
        reservationRepository.delete(reservation);
        ResponseEntity<ServiceResponse<RentUserDto>> response = null;
        RentUserDto client = null;
        try {
            response = userServiceRestTemplate.exchange("/findUserEmail/" + reservation.getClientId(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<ServiceResponse<RentUserDto>>() {});
            client = response.getBody().getData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServiceResponse<>(false, "user with a given id does not exist!", 404);
        }
        RentUserDto manager = getManager(reservation.getVehicle().getCarModel().getCompany().getId());
        NotificationMQ<PopCancel> q = new NotificationMQ<>();
        q.setType("CANCEL");
        q.setData(new PopCancel(client.getEmail(), manager.getEmail(), client.getUsername(),
                reservation.getVehicle().getCarModel().getModelName(), reservation.getVehicle().getLocation().getCity(),
                reservation.getVehicle().getCarModel().getCompany().getId(), reservation.getVehicle().getCarModel().getCompany().getCompanyName(),
                reservation.getStartTime(), reservation.getEndTime(), removeReservationDto.getId()));
        jmsTemplate.convertAndSend(notificationQueue, messageHelper.createTextMessage(q));
        updateUserRentDays(client.getId(), reservation.getStartTime() - reservation.getEndTime());
        return new ServiceResponse<>(true, "Reservation deleted", 200);
    }

    @Override
    public ServiceResponse<List<FilterInterval>> filterVehicles(VehicleFilter vehicleFilter) {
        List<VehicleDtoFull> vehicles = vehicleRepository.findAll().stream().
                filter(v -> (vehicleFilter.getCarTypeId() == null || v.getCarModel().getCarType().getId().equals(vehicleFilter.getCarTypeId()))
                        && (vehicleFilter.getLocationId() == null || v.getLocation().getId().equals(vehicleFilter.getLocationId()))
                        && (vehicleFilter.getCompanyId() == null || v.getCarModel().getCompany().getId().equals(vehicleFilter.getCompanyId())))
                .filter(v -> {
                    List<Reservation> reservations = reservationRepository.findAllByVehicle(v);
                    if(reservations.isEmpty()) { return true; }
                    for(Reservation r : reservations) {
                        System.out.println(r);
                        System.out.println(vehicleFilter.getStartTime() + " " + vehicleFilter.getEndTime());
                        if(!(r.getStartTime() < vehicleFilter.getStartTime() && r.getEndTime() < vehicleFilter.getStartTime())
                            && !(r.getStartTime() > vehicleFilter.getEndTime() && r.getEndTime() > vehicleFilter.getEndTime())){
                            return false;
                        }
                    }
                    System.out.println(v);
                    return true;
                }).map(vehicleMapper::vehicleToVehicleDtoFull).collect(Collectors.toList());
        List<FilterInterval> lista = new ArrayList<>();
        for(VehicleDtoFull vehicleDto : vehicles) {
            FilterInterval filterInterval = vehicleMapper.vehicleDtoToFilterInterval(vehicleDto);
            if(!lista.contains(filterInterval)) {
                lista.add(filterInterval);
            }
        }
        return new ServiceResponse<>(lista, "Vehicles found", 200);
    }

    @Override
    public ServiceResponse<List<ReservationDtoFull>> getUserReservations(String jwt) {
        Long userId = tokenService.getUserId(jwt);
        List<ReservationDtoFull> reservations = reservationRepository.findAllByClientId(userId).stream().
                map(reservationMapper::reservationToReservationMapFull).collect(Collectors.toList());
        return new ServiceResponse<>(reservations, "Reservations found", 200);
    }

    @Override
    public ServiceResponse<List<ReservationDtoFull>> getCompanyReservations(String jwt) {
        Long companyId = tokenService.getCompanyId(jwt);
        List<ReservationDtoFull> reservations = reservationRepository
                .findAllByCompany(companyRepository.getReferenceById(companyId).orElse(null)).stream().
                map(reservationMapper::reservationToReservationMapFull).collect(Collectors.toList());
        return new ServiceResponse<>(reservations, "Reservations found", 200);
    }


    @Override
    public ServiceResponse<List<GetVehicleDto>> getAvailableVehiclesDto(FindVehiclesDto findVehiclesDto) {
        return null;
    }

    private RentUserDto getManager(Long companyId) {
        RentUserDto manager = null;
        ResponseEntity<ServiceResponse<RentUserDto>> response = null;
        try {
            response = userServiceRestTemplate.exchange("/findManagerEmail/" + companyId,
                    HttpMethod.GET, null, new ParameterizedTypeReference<ServiceResponse<RentUserDto>>() {});
            manager = response.getBody().getData();
        } catch (Exception e) {
            return null;
        }
        return manager;
    }
    @Override
    public ServiceResponse<Boolean> addReview(String jwt, ReviewDto reviewDto) {
        Long userId = tokenService.getUserId(jwt);
        Reservation reservation = reservationRepository.findReservationById(reviewDto.getReservationId()).orElse(null);
        if (reservation == null) return new ServiceResponse<>(false, "given Reservation does not exist", 400);
        if (!Objects.equals(userId, reservation.getClientId()))
            return new ServiceResponse<>(false, "User with the given id did not make the given reservation", 400);
        Review review = null;
        review = reviewRepository.findReviewByReservation(reservation).orElse(null);
        if (review != null) return new ServiceResponse<>(false, "Review for given Reservation already exists", 404);
        if (reservation == null) {
            return new ServiceResponse<>(false, "Reservation not found", 404);
        }

        review = reviewMapper.reviewDtoToReview(reviewDto, userId);


        reviewRepository.save(review);
        return new ServiceResponse<>(true, "Review created", 201);
    }
    private void updateUserRentDays(Long userId, Integer timeDiff) {
        Integer daysDiff = timeDiff / 86400;
        HttpEntity<Integer> request = new HttpEntity<>(daysDiff);
        try {
            userServiceRestTemplate.exchange("/changeRentDays/" + userId,
                    HttpMethod.POST, request, new ParameterizedTypeReference<ServiceResponse<Boolean>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
