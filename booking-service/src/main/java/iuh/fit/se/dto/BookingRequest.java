package iuh.fit.se.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long userId;
    private Long movieId;
    private String movieTitle;
    private int numberOfSeats;
    private double totalPrice;
}
