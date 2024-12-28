package com.databaseproject.parkingproject.controller;

import com.databaseproject.parkingproject.dto.UserDto;
import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.dto.SigningDto;
import com.databaseproject.parkingproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/api/authenticate/signup")
    public ResponseMessageDto addUser(@RequestBody UserDto driverDto) {
        return userService.addUser(driverDto);
    }
    @PostMapping("/api/authenticate/signin")
    public ResponseMessageDto signIn(@RequestBody SigningDto signingDto) {
        return userService.signIn(signingDto);
    }
    @PostMapping("/api/authenticate/managerSignup")
    public ResponseMessageDto addManager(@RequestBody UserDto driverDto) {
        return userService.addManager(driverDto);
    }
}
