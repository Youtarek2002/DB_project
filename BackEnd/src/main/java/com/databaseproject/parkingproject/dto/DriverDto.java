package com.databaseproject.parkingproject.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DriverDto {
    private String fname;
    private String lname;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String licensePlate;
    private String paymentMethod;
}
