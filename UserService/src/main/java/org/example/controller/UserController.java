package org.example.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
//    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestHeader("Authorization") String authorization,
                                                     Pageable pageable) {
        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);

//        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }

//    @ApiOperation(value = "Register user")
//    @PostMapping
//    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
//        return new ResponseEntity<>(userService.add(userCreateDto), HttpStatus.CREATED);
//    }
//
//    @ApiOperation(value = "Login")
//    @PostMapping("/login")
//    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
//        return new ResponseEntity<>(userService.login(tokenRequestDto), HttpStatus.OK);
//    }
//    @ApiOperation(value = "Login")
//    @GetMapping("/login")
//    public ResponseEntity<String> proba(@RequestBody @Valid String token) {
//        return new ResponseEntity<>(token, HttpStatus.OK);
//    }
//
//    @ApiOperation(value = "Proba")
//    @GetMapping("/proba2")
//    public ResponseEntity<String> proba2() {
//        return new ResponseEntity<String>("ok", HttpStatus.OK);
//    }
}
