package com.databaseproject.parkingproject.dao;

import com.databaseproject.parkingproject.config.JwtService;
import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.dto.SigningDto;
import com.databaseproject.parkingproject.entity.Users;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.Map;

@AllArgsConstructor
@Repository
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final JwtService jwtService;



    @Override
    public ResponseMessageDto addUser(Users driver) {
        String countQuery = "SELECT COUNT(*) FROM users WHERE email = ? OR username = ?";
        long count = jdbcTemplate.queryForObject(countQuery, Long.class, driver.getEmail(), driver.getUsername());

        if (count > 0) {
            return ResponseMessageDto.builder()
                    .message("Driver already exists")
                    .success(false)
                    .statusCode(400)
                    .data(null)
                    .build();
        }

        // Insert new driver into the database
        String insertQuery = "INSERT INTO users (fname, lname, username, email, phone, password, payment_method, role, license_plate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertQuery,
                driver.getFname(),
                driver.getLname(),
                driver.getUsername(),
                driver.getEmail(),
                driver.getPhone(),
                driver.getPassword(), // Use hashed password
                driver.getPaymentMethod(),
                driver.getRole().toString(),
                driver.getLicensePlate());

        Map<String,Object> extraClaims=Map.of(
                "role", driver.getRole().name(),
                "firstName", driver.getClass(),
                "lastName", driver.getLname(),
                "email", driver.getClass(),
                "phone", driver.getPhone(),
                "licensePlate", driver.getLicensePlate()
        );

        var jwtToken=jwtService.generateToken(extraClaims,driver);

        return ResponseMessageDto.builder()
                .message("Driver added successfully")
                .success(true)
                .statusCode(200)
                .data(jwtToken)
                .build();
    }

    @Override
    public ResponseMessageDto signIn(SigningDto signingDto) {
        // Check if the user exists based on email or username
        String countQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
        long count = jdbcTemplate.queryForObject(countQuery, Long.class, signingDto.getEmail());

        if (count == 0) {
            return ResponseMessageDto.builder()
                    .message("User not found")
                    .success(false)
                    .statusCode(404)
                    .data(null)
                    .build();
        }

        // Check if the password is correct
        String passwordQuery = "SELECT password FROM users WHERE email = ?";
        String password = jdbcTemplate.queryForObject(passwordQuery, String.class, signingDto.getEmail());

        if (!password.equals(signingDto.getPassword())) {
            return ResponseMessageDto.builder()
                    .message("Incorrect password")
                    .success(false)
                    .statusCode(400)
                    .data(null)
                    .build();
        }
        String userQuery = "SELECT * FROM users WHERE email = ?";
        Users user = jdbcTemplate.queryForObject(userQuery, new UserRowMapper(), signingDto.getEmail());
        Map<String,Object> extraClaims=Map.of(
                "role", user.getRole().toString(),
                "firstName", user.getFname(),
                "lastName", user.getLname(),
                "email", user.getEmail(),
                "phone", user.getPhone(),
                "licensePlate", user.getLicensePlate(),
                "id",user.getId()
        );

        var jwtToken=jwtService.generateToken(extraClaims,user);
        return ResponseMessageDto.builder()
                .message("User signed in successfully")
                .success(true)
                .statusCode(200)
                .data(jwtToken)
                .build();
    }

    private class UserRowMapper implements RowMapper<Users> {
        @Override
        public Users mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            return Users.builder()
                    .id(rs.getInt("id"))
                    .fname(rs.getString("fname"))
                    .lname(rs.getString("lname"))
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .phone(rs.getString("phone"))
                    .password(rs.getString("password"))
                    .paymentMethod(rs.getString("payment_method"))
                    .role(Users.Role.valueOf(rs.getString("role")))
                    .licensePlate(rs.getString("license_plate"))
                    .build();
        }
    }
}
