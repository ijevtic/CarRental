package org.example.mapper;

import org.example.domain.User;
import org.example.dto.*;
import org.example.dto.rent.RentUserDto;
import org.example.repository.RoleRepository;
import org.example.repository.StateRepository;
import org.springframework.data.util.Pair;
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

    public UserChangeDto userToUserChangeDto(User user) {
        UserChangeDto userChangeDto = new UserChangeDto();
        userChangeDto.setEmail(user.getEmail());
        userChangeDto.setFirstName(user.getFirstName());
        userChangeDto.setLastName(user.getLastName());
        userChangeDto.setUsername(user.getUsername());
        userChangeDto.setPassportNumber(user.getPassportNumber());
        userChangeDto.setBirthDate(user.getBirthDate());
        userChangeDto.setPhoneNumber(user.getPhoneNumber());
        return userChangeDto;
    }

    public ManagerChangeDto userToManagerChangeDto(User user) {
        ManagerChangeDto managerChangeDto = new ManagerChangeDto();
        managerChangeDto.setEmail(user.getEmail());
        managerChangeDto.setFirstName(user.getFirstName());
        managerChangeDto.setLastName(user.getLastName());
        managerChangeDto.setUsername(user.getUsername());
        managerChangeDto.setBirthDate(user.getBirthDate());
        managerChangeDto.setPhoneNumber(user.getPhoneNumber());
        managerChangeDto.setStartWorkDate(user.getStartWorkDate());
        return managerChangeDto;
    }

    public Pair<ManagerChangeDto, Boolean> mergeManagerChangeDto(ManagerChangeDto old, ManagerChangeDto newUser) {
        ManagerChangeDto userChangeDto = new ManagerChangeDto();
        if(newUser.getEmail() != null) {
            userChangeDto.setEmail(newUser.getEmail());
        } else {
            userChangeDto.setEmail(old.getEmail());
        }
        if(newUser.getFirstName() != null) {
            userChangeDto.setFirstName(newUser.getFirstName());
        } else {
            userChangeDto.setFirstName(old.getFirstName());
        }
        if(newUser.getLastName() != null) {
            userChangeDto.setLastName(newUser.getLastName());
        } else {
            userChangeDto.setLastName(old.getLastName());
        }
        if(newUser.getUsername() != null) {
            userChangeDto.setUsername(newUser.getUsername());
        } else {
            userChangeDto.setUsername(old.getUsername());
        }
        if(newUser.getBirthDate() != null) {
            userChangeDto.setBirthDate(newUser.getBirthDate());
        } else {
            userChangeDto.setBirthDate(old.getBirthDate());
        }
        if(newUser.getPhoneNumber() != null) {
            userChangeDto.setPhoneNumber(newUser.getPhoneNumber());
        } else {
            userChangeDto.setPhoneNumber(old.getPhoneNumber());
        }
        if(newUser.getStartWorkDate() != null) {
            userChangeDto.setStartWorkDate(newUser.getStartWorkDate());
        } else {
            userChangeDto.setStartWorkDate(old.getStartWorkDate());
        }
        boolean changePassword = false;
        if(newUser.getPassword() != null) {
            userChangeDto.setPassword(newUser.getPassword());
            changePassword = true;
        } else {
            userChangeDto.setPassword(old.getPassword());
        }
        return Pair.of(userChangeDto, changePassword);
    }

    public Pair<UserChangeDto, Boolean> mergeUserChangeDto(UserChangeDto old, UserChangeDto newUser) {
        UserChangeDto userChangeDto = new UserChangeDto();
        if(newUser.getEmail() != null) {
            userChangeDto.setEmail(newUser.getEmail());
        } else {
            userChangeDto.setEmail(old.getEmail());
        }
        if(newUser.getFirstName() != null) {
            userChangeDto.setFirstName(newUser.getFirstName());
        } else {
            userChangeDto.setFirstName(old.getFirstName());
        }
        if(newUser.getLastName() != null) {
            userChangeDto.setLastName(newUser.getLastName());
        } else {
            userChangeDto.setLastName(old.getLastName());
        }
        if(newUser.getUsername() != null) {
            userChangeDto.setUsername(newUser.getUsername());
        } else {
            userChangeDto.setUsername(old.getUsername());
        }
        if(newUser.getPassportNumber() != null) {
            userChangeDto.setPassportNumber(newUser.getPassportNumber());
        } else {
            userChangeDto.setPassportNumber(old.getPassportNumber());
        }
        if(newUser.getBirthDate() != null) {
            userChangeDto.setBirthDate(newUser.getBirthDate());
        } else {
            userChangeDto.setBirthDate(old.getBirthDate());
        }
        if(newUser.getPhoneNumber() != null) {
            userChangeDto.setPhoneNumber(newUser.getPhoneNumber());
        } else {
            userChangeDto.setPhoneNumber(old.getPhoneNumber());
        }
        boolean changePassword = false;
        if(newUser.getPassword() != null) {
            userChangeDto.setPassword(newUser.getPassword());
            changePassword = true;
        } else {
            userChangeDto.setPassword(old.getPassword());
        }
        return Pair.of(userChangeDto, changePassword);
    }

    public User userChangeDtoToUser(UserChangeDto userChangeDto) {
        User user = new User();
        user.setEmail(userChangeDto.getEmail());
        user.setFirstName(userChangeDto.getFirstName());
        user.setLastName(userChangeDto.getLastName());
        user.setUsername(userChangeDto.getUsername());
        user.setPassportNumber(userChangeDto.getPassportNumber());
        user.setBirthDate(userChangeDto.getBirthDate());
        user.setPhoneNumber(userChangeDto.getPhoneNumber());
        user.setPassword(userChangeDto.getPassword());
        return user;
    }

    public RentUserDto userToRentUserDto(User user) {
        RentUserDto rentUserDto = new RentUserDto();
        rentUserDto.setId(user.getId());
        rentUserDto.setEmail(user.getEmail());
        rentUserDto.setFirstName(user.getFirstName());
        rentUserDto.setLastName(user.getLastName());
        rentUserDto.setUsername(user.getUsername());
        return rentUserDto;
    }

    public UserAllData userToUserAllData(User user) {
        UserAllData userAllData = new UserAllData();
        userAllData.setId(user.getId());
        userAllData.setEmail(user.getEmail());
        userAllData.setFirstName(user.getFirstName());
        userAllData.setLastName(user.getLastName());
        userAllData.setUsername(user.getUsername());
        userAllData.setPassword(user.getPassword());
        userAllData.setRoleId(user.getRole().getId());
        userAllData.setStateId(user.getState().getId());
        userAllData.setPassportNumber(user.getPassportNumber());
        userAllData.setBirthDate(user.getBirthDate());
        userAllData.setTotalRentDays(user.getTotalRentDays());
        userAllData.setCompanyId(user.getCompanyId());
        userAllData.setPhoneNumber(user.getPhoneNumber());
        userAllData.setStartWorkDate(user.getStartWorkDate());
        return userAllData;
    }
}
