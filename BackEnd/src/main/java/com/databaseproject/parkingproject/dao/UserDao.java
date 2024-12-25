package com.databaseproject.parkingproject.dao;

import com.databaseproject.parkingproject.dto.DriverDto;
import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.dto.SigningDto;
import com.databaseproject.parkingproject.entity.Users;

public interface UserDao {
    public ResponseMessageDto addDriver(Users driver);
    public ResponseMessageDto signIn(SigningDto signingDto);
}
