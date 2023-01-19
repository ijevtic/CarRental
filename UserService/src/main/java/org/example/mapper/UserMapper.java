package org.example.mapper;

import org.example.domain.User;
import org.example.dto.AUserCreateDto;
import org.example.dto.ManagerCreateDto;
import org.example.dto.UserCreateDto;
import org.example.dto.UserDto;
import org.example.repository.RoleRepository;
import org.example.repository.StateRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private RoleRepository roleRepository;
    private StateRepository stateRepository;

    public UserMapper(RoleRepository roleRepository, StateRepository stateRepository) {
        this.roleRepository = roleRepository;
        this.stateRepository = stateRepository;
    }

    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        //TODO
        userDto.setRole(user.getRole());
        userDto.setState(user.getState());
        return userDto;
    }
    public User userCreateDtoToUser(UserCreateDto userCreateDto) {
        User user = userToUserCreateDtoBase(userCreateDto);
        user.setRole(roleRepository.findRoleByName("USER").get());
        user.setPassportNumber(userCreateDto.getPassportNumber());
        user.setTotalRentDays(0);
        return user;
    }
    public User managerCreateDtoToManager(ManagerCreateDto managerCreateDto) {
        User manager = userToUserCreateDtoBase(managerCreateDto);
        manager.setRole(roleRepository.findRoleByName("MANAGER").get());
//        manager.setCompanyName(managerCreateDto.getCompanyName());
        manager.setStartWorkDate(managerCreateDto.getStartWorkDate());
        return manager;
    }

    private User userToUserCreateDtoBase(AUserCreateDto userCreateDto) {
        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        user.setState(stateRepository.findStateByName("NOT_VERIFIED").get());
        return user;
    }
}
