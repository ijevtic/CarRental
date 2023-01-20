package org.example.service;

import org.example.dto.*;
import org.example.util.ServiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ServiceResponse<Page<UserDto>> findAll(Pageable pageable);

    ServiceResponse<Boolean> findUser(Long userId);

    ServiceResponse<Boolean> banUser(String username);

    ServiceResponse<Boolean> unbanUser(String username);

    ServiceResponse<Boolean> addUser(UserCreateDto userCreateDto);

    ServiceResponse<Boolean> addManager(ManagerCreateDto managerCreateDto);

    ServiceResponse<Boolean> changeUser(String jwt, UserChangeDto userChangeDto);

    ServiceResponse<Boolean> changeManager(String jwt, ManagerChangeDto managerChangeDto);

    ServiceResponse<TokenResponseDto> login(TokenRequestDto tokenRequestDto);

    ServiceResponse<Boolean> verifyUser(PendingUserDto pendingUserDto);
}

