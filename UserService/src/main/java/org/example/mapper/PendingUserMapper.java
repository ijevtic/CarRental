package org.example.mapper;

import org.example.domain.PendingUser;
import org.example.domain.User;
import org.example.dto.PendingUserDto;
import org.example.dto.UserCreateDto;
import org.example.dto.UserDto;
import org.example.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class PendingUserMapper {

    public PendingUserMapper() {}

    public PendingUserDto pendingUserToPendingUserDto(PendingUser user) {
        PendingUserDto UserDto = new PendingUserDto();
        UserDto.setEmail(user.getEmail());
        UserDto.setVerificationCode(user.getVerificationCode());
        return UserDto;
    }
    public PendingUser pendingUserDtoToUserDto(PendingUserDto userDto) {
        PendingUser user = new PendingUser();
        user.setEmail(userDto.getEmail());
        user.setVerificationCode(userDto.getVerificationCode());
        return user;
    }
}
