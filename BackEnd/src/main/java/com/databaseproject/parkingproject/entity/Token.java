package com.databaseproject.parkingproject.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public class Token {
    private Integer id;
    private String token;
    private boolean revoked;
    private Integer userId;

    public Token(Integer id, String token, boolean revoked, Integer userId) {
        this.id = id;
        this.token = token;
        this.revoked = revoked;
        this.userId = userId;
    }

    public Token() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", revoked=" + revoked +
                ", userId=" + userId +
                '}';
    }
}

