package com.databaseproject.parkingproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseMessageDto {
    private String message;
    private boolean success;
    private int statusCode;
    private Object data;
}
