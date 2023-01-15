package org.example.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.example.domain.PendingUser;
import org.example.domain.Role;
import org.example.domain.User;
import org.example.dto.*;
import org.example.exception.NotFoundException;
import org.example.mapper.PendingUserMapper;
import org.example.mapper.UserMapper;
import org.example.repository.PendingUserRepository;
import org.example.repository.UserRepository;
import org.example.security.service.TokenService;
import org.example.service.UserService;
import org.example.util.ServiceResponse;
import org.example.util.UtilClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private TokenService tokenService;
    private UserRepository userRepository;
    private PendingUserRepository pendingUserRepository;
    private UserMapper userMapper;
    private PendingUserMapper pendingUserMapper;

    public UserServiceImpl(UserRepository userRepository, TokenService tokenService, PendingUserRepository pendingUserRepository,
                           UserMapper userMapper, PendingUserMapper pendingUserMapper) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.pendingUserRepository = pendingUserRepository;
        this.userMapper = userMapper;
        this.pendingUserMapper = pendingUserMapper;
    }

    @Override
    public ServiceResponse<Page<UserDto>> findAll(Pageable pageable) {
        return new ServiceResponse<>(userRepository.findAll(pageable).map(userMapper::userToUserDto),
                "all users", 200);
    }

    private UserDto findByUsernameOrEmail(String username, String email) {
        return userRepository.findUserByUsernameOrEmail(username, email)
                .map(userMapper::userToUserDto)
                .orElse(null);
    }

    @Override
    public ServiceResponse<Boolean> addUser(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);
        if(findByUsernameOrEmail(user.getUsername(), user.getEmail()) != null) {
            return new ServiceResponse<>(null, "user already exists", 400);
        }
        userRepository.save(user);
        PendingUser pendingUser = new PendingUser();
        pendingUser.setEmail(user.getEmail());
        pendingUser.setVerificationCode(UtilClass.generateRandomString());
        pendingUserRepository.save(pendingUser);
        return new ServiceResponse<>(null, "user added", 200);
    }

    @Override
    public ServiceResponse<Boolean> addManager(ManagerCreateDto managerCreateDto) {
        User manager = userMapper.managerCreateDtoToManager(managerCreateDto);
        if(findByUsernameOrEmail(manager.getUsername(), manager.getEmail()) != null) {
            return new ServiceResponse<>(null, "manager already exists", 400);
        }
        userRepository.save(manager);
        PendingUser pendingUser = new PendingUser();
        pendingUser.setEmail(manager.getEmail());
        pendingUser.setVerificationCode(UtilClass.generateRandomString());
        pendingUserRepository.save(pendingUser);
        return new ServiceResponse<>(null, "manager added", 200);
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
        if(user.isForbidden() || !user.isEnabled()) {
            return new ServiceResponse<>(null, "User is forbidden or not enabled.", 401);
        }
        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().getName());
        //Generate token
        TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenService.generate(claims));
        return new ServiceResponse<>(tokenResponseDto, "Login successful", 200);
    }

    private PendingUserDto findByEmailAndVerificationCode(String email, String verificationCode) {
        return pendingUserRepository.findPendingUserByEmailAndVerificationCode(email, verificationCode)
                .map(pendingUserMapper::pendingUserToPendingUserDto)
                .orElse(null);
    }

    @Override
    public ServiceResponse<Boolean> verifyUser(PendingUserDto pendingUserDto) {
        if(findByEmailAndVerificationCode(pendingUserDto.getEmail(), pendingUserDto.getVerificationCode()) != null) {
            pendingUserRepository.deletePendingUserByEmail(pendingUserDto.getEmail());
            userRepository.enableUser(pendingUserDto.getEmail());
            return new ServiceResponse<>(true, "User verified", 200);
        }
        return new ServiceResponse<>(false, "User not verified", 400);
    }
}