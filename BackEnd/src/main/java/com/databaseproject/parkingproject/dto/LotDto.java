package com.databaseproject.parkingproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LotDto {
    private long id;
    private int disabled_count;
    private int regular_count;
    private int EV_count;
    private String location;
    private double latitude;
    private double longitude;
}
