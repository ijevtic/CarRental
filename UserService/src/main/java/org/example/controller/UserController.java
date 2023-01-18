package org.example.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.example.dto.*;
import org.example.listener.helper.MessageHelper;
import org.example.security.CheckSecurity;
import org.example.service.UserService;
import org.example.util.ServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private String orderDestination;

    public UserController(UserService userService, JmsTemplate jmsTemplate, MessageHelper messageHelper,
                          @Value("${destination.createOrder}") String orderDestination) {
        this.userService = userService;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.orderDestination = orderDestination;
    }

    @ApiOperation(value = "Get all users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping
    @CheckSecurity(roles = {"ADMIN", "USER"})
    public ResponseEntity<ServiceResponse<Page<UserDto>>> getAllUsers(@RequestHeader("Authorization") String authorization,
                                                     Pageable pageable) {
        ServiceResponse<Page<UserDto>> response = userService.findAll(pageable);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @ApiOperation(value = "Ban user")
    @PostMapping("/ban/{username}")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<ServiceResponse<Boolean>> forbidUser(@RequestHeader("Authorization") String authorization
            , @PathVariable("username") String username) {
        ServiceResponse<Boolean> response = userService.banUser(username);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @ApiOperation(value = "Unban user")
    @PostMapping("/unban/{username}")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<ServiceResponse<Boolean>> unforbidUser(@RequestHeader("Authorization") String authorization
            , @PathVariable("username") String username) {
        ServiceResponse<Boolean> response = userService.unbanUser(username);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @ApiOperation(value = "Register user")
    @PostMapping("/registerUser")
    public ResponseEntity<ServiceResponse<Boolean>> saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        ServiceResponse<Boolean> response = userService.addUser(userCreateDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @ApiOperation(value = "Register manager")
    @PostMapping("/registerManager")
    public ResponseEntity<ServiceResponse<Boolean>> saveManager(@RequestBody @Valid ManagerCreateDto managerCreateDto) {
        ServiceResponse<Boolean> response = userService.addManager(managerCreateDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<ServiceResponse<TokenResponseDto>> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        ServiceResponse<TokenResponseDto> response = userService.login(tokenRequestDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @ApiOperation(value = "Verify")
    @GetMapping("/verify")
    public ResponseEntity<ServiceResponse<Boolean>> verifyUser(@RequestBody @Valid PendingUserDto pendingUserDto) {
        ServiceResponse<Boolean> response = userService.verifyUser(pendingUserDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @ApiOperation(value = "Lol")
    @GetMapping("/lol")
    public ResponseEntity<ServiceResponse<Boolean>> lol() {
        jmsTemplate.convertAndSend(orderDestination, messageHelper.createTextMessage("lol"));
        return new ResponseEntity<>(new ServiceResponse<>(true,"aa",200), HttpStatus.OK);
    }
}
