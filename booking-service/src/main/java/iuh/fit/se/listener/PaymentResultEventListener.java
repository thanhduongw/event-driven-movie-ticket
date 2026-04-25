package iuh.fit.se.listener;

import iuh.fit.se.config.RabbitMQConfig;
import iuh.fit.se.model.Booking;
import iuh.fit.se.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentResultEventListener {
    private final BookingService bookingService;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_COMPLETED_QUEUE)
    public void handlePaymentCompleted(Map<String, Object> event) {
        Long bookingId = Long.valueOf(event.get("bookingId").toString());
        bookingService.updateBookingStatus(bookingId, Booking.BookingStatus.CONFIRMED);
        System.out.println("[BOOKING-SERVICE] Updated booking #" + bookingId + " to CONFIRMED");
    }

    @RabbitListener(queues = RabbitMQConfig.BOOKING_FAILED_QUEUE)
    public void handleBookingFailed(Map<String, Object> event) {
        Long bookingId = Long.valueOf(event.get("bookingId").toString());
        bookingService.updateBookingStatus(bookingId, Booking.BookingStatus.FAILED);
        System.out.println("[BOOKING-SERVICE] Updated booking #" + bookingId + " to FAILED");
    }
}

