package iuh.fit.se.listener;

import iuh.fit.se.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationEventListener {

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_COMPLETED_QUEUE)
    public void handlePaymentCompleted(Map<String, Object> event) {
        Long bookingId = Long.valueOf(event.get("bookingId").toString());
        Long userId = Long.valueOf(event.get("userId").toString());
        String movieTitle = event.get("movieTitle").toString();

        String message = String.format(
                "[NOTIFICATION] Booking #%d thành công! User %d đã đặt vé xem '%s'",
                bookingId, userId, movieTitle
        );
        System.out.println(message);
        System.out.println("[NOTIFICATION] Gửi email/SMS đến user " + userId + "...");
    }

    @RabbitListener(queues = RabbitMQConfig.BOOKING_FAILED_QUEUE)
    public void handleBookingFailed(Map<String, Object> event) {
        Long bookingId = Long.valueOf(event.get("bookingId").toString());
        String reason = event.get("reason").toString();

        System.out.println(String.format(
                "[NOTIFICATION] Booking #%d THẤT BẠI! Lý do: %s", bookingId, reason
        ));
    }
}
