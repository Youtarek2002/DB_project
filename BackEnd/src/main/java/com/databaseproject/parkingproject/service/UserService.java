package com.databaseproject.parkingproject.service;

import com.databaseproject.parkingproject.dto.DriverDto;
import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.dto.SigningDto;

public interface UserService {
    public ResponseMessageDto addDriver(DriverDto driverDto);
    public ResponseMessageDto signIn(SigningDto signingDto);
}
