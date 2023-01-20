package org.example.controller;

import io.swagger.annotations.ApiOperation;
import org.example.service.NotificationService;
import org.example.util.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ApiOperation(value = "Send Email")
    @PostMapping("/login")
    public ResponseEntity<String> sendEmail(@RequestBody @Valid String email) {
//        notificationService.sendEmail(email);
        return new ResponseEntity<>("response", HttpStatus.OK);
    }
}
