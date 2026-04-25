package iuh.fit.se.service;

import iuh.fit.se.config.RabbitMQConfig;
import iuh.fit.se.dto.BookingRequest;
import iuh.fit.se.model.Booking;
import iuh.fit.se.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RabbitTemplate rabbitTemplate;

    public Booking createBooking(BookingRequest request) {
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setMovieId(request.getMovieId());
        booking.setMovieTitle(request.getMovieTitle());
        booking.setNumberOfSeats(request.getNumberOfSeats());
        booking.setTotalPrice(request.getTotalPrice());
        booking.setStatus(Booking.BookingStatus.PENDING);

        Booking saved = bookingRepository.save(booking);

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "BOOKING_CREATED");
        event.put("bookingId", saved.getId());
        event.put("userId", saved.getUserId());
        event.put("movieId", saved.getMovieId());
        event.put("movieTitle", saved.getMovieTitle());
        event.put("numberOfSeats", saved.getNumberOfSeats());
        event.put("totalPrice", saved.getTotalPrice());
        event.put("timestamp", System.currentTimeMillis());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.BOOKING_EXCHANGE,
                RabbitMQConfig.BOOKING_CREATED_KEY,
                event
        );
        System.out.println("[BOOKING-SERVICE] Published BOOKING_CREATED for booking #" + saved.getId());
        return saved;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void updateBookingStatus(Long bookingId, Booking.BookingStatus status) {
        bookingRepository.findById(bookingId).ifPresent(booking -> {
            booking.setStatus(status);
            bookingRepository.save(booking);
        });
    }
}
