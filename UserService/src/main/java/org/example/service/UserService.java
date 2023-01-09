package org.example.service;

import org.example.dto.TokenRequestDto;
import org.example.dto.TokenResponseDto;
import org.example.dto.UserCreateDto;
import org.example.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserDto> findAll(Pageable pageable);

    UserDto add(UserCreateDto userCreateDto);

    UserDto proba();

    TokenResponseDto login(TokenRequestDto tokenRequestDto);
}

