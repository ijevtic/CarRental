package org.example.service;

import org.example.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {

    Page<UserDto> findAll(Pageable pageable);

    boolean add(UserCreateDto userCreateDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    boolean verifyUser(PendingUserDto pendingUserDto);
}

