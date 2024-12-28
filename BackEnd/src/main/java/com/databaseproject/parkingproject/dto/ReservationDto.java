package com.databaseproject.parkingproject.dto;

import com.databaseproject.parkingproject.entity.Reservations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    String location;
    Double longitude;
    Double latitude;
    List<Reservations> reservations;

    public void setReservation(Reservations reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        this.reservations.add(reservation);
}
}