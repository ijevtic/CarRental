package org.example.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.example.domain.User;
import org.example.dto.TokenRequestDto;
import org.example.dto.TokenResponseDto;
import org.example.dto.UserCreateDto;
import org.example.dto.UserDto;
import org.example.exception.NotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.security.service.TokenService;
import org.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private TokenService tokenService;
    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, TokenService tokenService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return null;
//        return userRepository.findAll(pageable)
//                .map(userMapper::userToUserDto);
    }

    @Override
    public UserDto add(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto proba() {
        User u = new User();
        u.setEmail("ivanjevtic@gmail.com");
        u.setFirstName("ivan");
        userRepository.save(u);
        return null;
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        //Try to find active user for specified credentials
        User user = userRepository
                .findUserByUsernameAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and password: %s not found.", tokenRequestDto.getUsername(),
                                tokenRequestDto.getPassword())));
        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().getName());
        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }
}