package com.databaseproject.parkingproject.controller;

import com.databaseproject.parkingproject.dto.DriverDto;
import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.dto.SigningDto;
import com.databaseproject.parkingproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/authenticate/signup/driver")
    public ResponseMessageDto addDriver(@RequestBody DriverDto driverDto) {
        return userService.addDriver(driverDto);
    }
    @PostMapping("/api/authenticate/signin")
    public ResponseMessageDto signIn(@RequestBody SigningDto signingDto) {
        return userService.signIn(signingDto);
    }

}
