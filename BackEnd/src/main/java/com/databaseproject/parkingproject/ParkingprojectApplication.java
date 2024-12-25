package com.databaseproject.parkingproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ParkingprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingprojectApplication.class, args);
	}

}
