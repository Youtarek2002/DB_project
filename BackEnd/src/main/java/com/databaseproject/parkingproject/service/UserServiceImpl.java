package com.databaseproject.parkingproject.service;

import com.databaseproject.parkingproject.dao.UserDao;
import com.databaseproject.parkingproject.dto.UserDto;
import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.dto.SigningDto;
import com.databaseproject.parkingproject.entity.Users;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public ResponseMessageDto addUser(UserDto driverDto) {
        var driver= Users.builder()
                .email(driverDto.getEmail())
                .password(driverDto.getPassword())
                .fname(driverDto.getFname())
                .lname(driverDto.getLname())
                .username(driverDto.getUsername())
                .phone(driverDto.getPhone())
                .licensePlate(driverDto.getLicensePlate())
                .paymentMethod(driverDto.getPaymentMethod())
                .role(Users.Role.valueOf("DRIVER"))
                .build();
        return userDao.addUser(driver);
    }

    @Override
    public ResponseMessageDto signIn(SigningDto signingDto) {
        return userDao.signIn(signingDto);
    }

}
