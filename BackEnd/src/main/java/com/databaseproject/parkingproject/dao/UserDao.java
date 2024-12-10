package com.databaseproject.parkingproject.dao;

import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.dto.SigningDto;
import com.databaseproject.parkingproject.entity.Users;

public interface UserDao {
    public ResponseMessageDto addUser(Users driver);
    public ResponseMessageDto signIn(SigningDto signingDto);
}
