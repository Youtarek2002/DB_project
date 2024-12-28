package com.databaseproject.parkingproject.service;

import com.databaseproject.parkingproject.dto.UserDto;
import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.dto.SigningDto;

public interface UserService {
    public ResponseMessageDto addUser(UserDto driverDto);
    public ResponseMessageDto signIn(SigningDto signingDto);
    public ResponseMessageDto addManager(UserDto driverDto);
}
