package org.example.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.example.client.notificationservice.NotificationMQ;
import org.example.client.notificationservice.pop.PopActivateAccount;
import org.example.client.notificationservice.pop.PopPassword;
import org.example.client.rentservice.EditCompanyDto;
import org.example.domain.*;
import org.example.dto.*;
import org.example.dto.rent.RentUserDto;
import org.example.listener.helper.MessageHelper;
import org.example.mapper.PendingUserMapper;
import org.example.mapper.UserMapper;
import org.example.repository.PendingUserRepository;
import org.example.repository.RoleRepository;
import org.example.repository.StateRepository;
import org.example.repository.UserRepository;
import org.example.security.service.TokenService;
import org.example.service.UserService;
import org.example.util.ServiceResponse;
import org.example.util.UtilClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private TokenService tokenService;
    private UserRepository userRepository;
    private PendingUserRepository pendingUserRepository;
    private UserMapper userMapper;
    private PendingUserMapper pendingUserMapper;
    private RoleRepository roleRepository;
    private StateRepository stateRepository;
    private RestTemplate rentServiceRestTemplate;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private String notificationQueue;

    public UserServiceImpl(TokenService tokenService, UserRepository userRepository, PendingUserRepository pendingUserRepository,
                           UserMapper userMapper, PendingUserMapper pendingUserMapper, RoleRepository roleRepository,
                           StateRepository stateRepository, RestTemplate rentServiceRestTemplate, JmsTemplate jmsTemplate,
                           MessageHelper messageHelper, @Value("${async.notifications}") String notificationQueue) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.pendingUserRepository = pendingUserRepository;
        this.userMapper = userMapper;
        this.pendingUserMapper = pendingUserMapper;
        this.roleRepository = roleRepository;
        this.stateRepository = stateRepository;
        this.rentServiceRestTemplate = rentServiceRestTemplate;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.notificationQueue = notificationQueue;
    }

    @Override
    public ServiceResponse<Page<UserDto>> findAll(Pageable pageable) {
        return new ServiceResponse<>(userRepository.findAll(pageable).map(userMapper::userToUserDto),
                "all users", 200);
    }

    @Override
    public ServiceResponse<Boolean> findUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ServiceResponse<>(false, "user not found", 404);
        }
        return new ServiceResponse<>(true, "user found", 200);
    }

    @Override
    public ServiceResponse<RentUserDto> findUserEmail(Long userId) {
        RentUserDto user = userRepository.findById(userId).map(userMapper::userToRentUserDto).orElse(null);
        if (user == null) {
            return new ServiceResponse<>(null, "user not found", 404);
        }
        return new ServiceResponse<>(user, "user found", 200);
    }

    @Override
    public ServiceResponse<RentUserDto> findManagerEmailFromCompany(Long companyId) {
        RentUserDto user = userRepository.findUserByCompanyId(companyId).map(userMapper::userToRentUserDto).orElse(null);
        if (user == null) {
            return new ServiceResponse<>(null, "user not found", 404);
        }
        return new ServiceResponse<>(user, "user found", 200);
    }

    private State findStateById(EState state) {
        return stateRepository.getReferenceById(State.stateMap.get(state))
                .orElse(null);
    }

    private UserDto findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findUserByUsernameOrEmail(usernameOrEmail)
                .map(userMapper::userToUserDto)
                .orElse(null);
    }

    @Override
    public ServiceResponse<Boolean> banUser(String username) {
        UserDto userDto = findByUsernameOrEmail(username);

        if (userDto == null) {
            return new ServiceResponse<>(false, "user not found", 404);
        }
        if (userDto.getState().getNameEnum() == EState.BAN || userDto.getState().getNameEnum() == EState.BAN_AND_NOT_VERIFIED) {
            return new ServiceResponse<>(false, "User is already banned", 400);
        }
        State newState = findStateById(EState.BAN);
        if (userDto.getState().getNameEnum() == EState.NOT_VERIFIED) {
            newState = findStateById(EState.BAN_AND_NOT_VERIFIED);
        }
        userRepository.updateState(userDto.getEmail(), newState);
        return new ServiceResponse<>(true, "user banned", 200);
    }

    @Override
    public ServiceResponse<Boolean> unbanUser(String username) {
        UserDto userDto = findByUsernameOrEmail(username);

        if (userDto == null) {
            return new ServiceResponse<>(false, "user not found", 404);
        }
        if (userDto.getState().getNameEnum() != EState.BAN && userDto.getState().getNameEnum() != EState.BAN_AND_NOT_VERIFIED) {
            return new ServiceResponse<>(false, "User is already not banned", 400);
        }
        State newState = findStateById(EState.OK);
        if (userDto.getState().getNameEnum() == EState.BAN_AND_NOT_VERIFIED) {
            newState = findStateById(EState.NOT_VERIFIED);
        }
        userRepository.updateState(userDto.getEmail(), newState);
        return new ServiceResponse<>(true, "user unbanned", 200);
    }


    @Override
    public ServiceResponse<Boolean> addUser(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);
        if(findByUsernameOrEmail(user.getUsername()) != null || findByUsernameOrEmail(user.getEmail()) != null) {
            return new ServiceResponse<>(null, "user already exists", 400);
        }
        userRepository.save(user);
        PendingUser pendingUser = new PendingUser();
        pendingUser.setEmail(user.getEmail());
        pendingUser.setVerificationCode(UtilClass.generateRandomString());
        pendingUserRepository.save(pendingUser);
        NotificationMQ<PopActivateAccount> q = new NotificationMQ<>();
        q.setType("ACTIVATE");
        q.setData(new PopActivateAccount(user.getEmail(), user.getUsername(), pendingUser.getVerificationCode()));
        jmsTemplate.convertAndSend(notificationQueue, messageHelper.createTextMessage(q));
        return new ServiceResponse<>(null, "user added", 200);
    }

    @Override
    public ServiceResponse<Boolean> addManager(ManagerCreateDto managerCreateDto) {
        User manager = userMapper.managerCreateDtoToManager(managerCreateDto);
        if(findByUsernameOrEmail(manager.getUsername()) != null || findByUsernameOrEmail(manager.getEmail()) != null) {
            return new ServiceResponse<>(null, "manager already exists", 400);
        }
        ResponseEntity<ServiceResponse<Long>> response = null;
        Long companyId = null;
        try {
            response = rentServiceRestTemplate.exchange("/getCompany/" + managerCreateDto.getCompanyName(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<ServiceResponse<Long>>() {});
            companyId = response.getBody().getData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServiceResponse<>(null, "company with a given name does not exist!", 404);
        }

        manager.setCompanyId(companyId);
        userRepository.save(manager);
        PendingUser pendingUser = new PendingUser();
        pendingUser.setEmail(manager.getEmail());
        pendingUser.setVerificationCode(UtilClass.generateRandomString());
        pendingUserRepository.save(pendingUser);
        return new ServiceResponse<>(null, "manager added", 200);
    }

    @Override
    public ServiceResponse<Boolean> changeUser(String jwt, UserChangeDto userChangeDto) {
        Pair<String, Long> userInfo = tokenService.getUserInfo(jwt);
        UserChangeDto userOldDto = userRepository.findById(userInfo.getSecond()).map(userMapper::userToUserChangeDto).orElse(null);
        if(userChangeDto == null) {
            return new ServiceResponse<>(false, "user not found", 404);
        }
        Pair<UserChangeDto, Boolean> info = userMapper.mergeUserChangeDto(userOldDto, userChangeDto);
        UserChangeDto u = info.getFirst();
        userRepository.updateUser(userInfo.getSecond(), u.getEmail(),u.getUsername(), u.getFirstName(),
                u.getLastName(), u.getPassword(), u.getPhoneNumber(), u.getBirthDate(), u.getPassportNumber());
        if(info.getSecond()) {
            NotificationMQ<PopPassword> q = new NotificationMQ<>();
            q.setType("PASSWORD");
            q.setData(new PopPassword(u.getEmail(), u.getUsername()));
            jmsTemplate.convertAndSend(notificationQueue, messageHelper.createTextMessage(q));
        }
        return new ServiceResponse<>(true, "user changed", 200);
    }

    @Override
    public ServiceResponse<Boolean> changeManager(String jwt, ManagerChangeDto managerChangeDto) {
        Pair<String, Long> userInfo = tokenService.getUserInfo(jwt);
        Long managerId = tokenService.getUserId(jwt);
        ManagerChangeDto managerOldDto = userRepository.findById(managerId).map(userMapper::userToManagerChangeDto).orElse(null);
        if(managerOldDto == null) {
            return new ServiceResponse<>(false, "manager not found", 404);
        }
        Pair<ManagerChangeDto, Boolean> info = userMapper.mergeManagerChangeDto(managerOldDto, managerChangeDto);
        ManagerChangeDto u = info.getFirst();
        userRepository.updateManager(managerId, u.getEmail(),u.getUsername(), u.getFirstName(),
                u.getLastName(), u.getPassword(), u.getPhoneNumber(), u.getBirthDate(), u.getStartWorkDate());
        if(info.getSecond()) {
            NotificationMQ<PopPassword> q = new NotificationMQ<>();
            q.setType("PASSWORD");
            q.setData(new PopPassword(u.getEmail(), u.getUsername()));
            jmsTemplate.convertAndSend(notificationQueue, messageHelper.createTextMessage(q));
        }
        if(managerChangeDto.getCompanyName() != null) {
            Boolean ok = false;
            ResponseEntity<ServiceResponse<Boolean>> response = null;
            HttpEntity<EditCompanyDto> requestEntity = new HttpEntity<>(new EditCompanyDto(userInfo.getSecond(), managerChangeDto.getCompanyName(), null));
            try {
                response = rentServiceRestTemplate.exchange("/editCompanyDesc/",
                        HttpMethod.POST, requestEntity,
                        new ParameterizedTypeReference<ServiceResponse<Boolean>>() {});
                ok = response.getBody().getData();
            } catch (Exception e) {
                e.printStackTrace();
                return new ServiceResponse<>(null, "company with a given name does not exist!", 404);
            }
        }
        return new ServiceResponse<>(true, "user changed", 200);
    }

    @Override
    public ServiceResponse<TokenResponseDto> login(TokenRequestDto tokenRequestDto) {
        //Try to find active user for specified credentials
        User user = userRepository
                .findUserByUsernameAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElse(null);
        if(user == null) {
            return new ServiceResponse<>(null, "Invalid credentials", 404);
        }
        if(user.getState().getNameEnum() != EState.OK) {
            return new ServiceResponse<>(null, "User is forbidden or not enabled.", 401);
        }
        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().getName());
        //TODO
        if(ERole.valueOf(user.getRole().getName()) == ERole.MANAGER) {
            claims.put("company_id", user.getCompanyId());
        }
        claims.put("time", System.currentTimeMillis() / 1000);
        //Generate token
        TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenService.generate(claims));
        return new ServiceResponse<>(tokenResponseDto, "Login successful", 200);
    }

    private PendingUserDto findByEmailAndVerificationCode(String email, String verificationCode) {
        return pendingUserRepository.findPendingUserByEmailAndVerificationCode(email, verificationCode)
                .map(pendingUserMapper::pendingUserToPendingUserDto)
                .orElse(null);
    }

    //TODO ako ima vise puta u bazi?
    @Override
    public ServiceResponse<Boolean> verifyUser(PendingUserDto pendingUserDto) {
        if(findByEmailAndVerificationCode(pendingUserDto.getEmail(), pendingUserDto.getVerificationCode()) != null) {
            UserDto userDto = findByUsernameOrEmail(pendingUserDto.getEmail());
            System.out.println("aaa");
            System.out.println(userDto);

            if(userDto.getState().getNameEnum() == EState.BAN_AND_NOT_VERIFIED ||
                userDto.getState().getNameEnum() == EState.BAN) {
                return new ServiceResponse<>(false, "User is banned and cant be verified", 400);
            }
            if(userDto.getState().getNameEnum() == EState.OK) {
                return new ServiceResponse<>(false, "User is already verified", 400);
            }
            pendingUserRepository.deletePendingUserByEmail(pendingUserDto.getEmail());
            userRepository.updateState(userDto.getEmail(), findStateById(EState.OK));
            return new ServiceResponse<>(true, "User verified", 200);
        }
        return new ServiceResponse<>(false, "User not verified, wrong verification code", 400);
    }
}