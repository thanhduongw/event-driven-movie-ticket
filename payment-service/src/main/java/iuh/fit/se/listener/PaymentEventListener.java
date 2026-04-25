package iuh.fit.se.listener;

import iuh.fit.se.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;

    @RabbitListener(queues = RabbitMQConfig.BOOKING_CREATED_QUEUE)
    public void handleBookingCreated(Map<String, Object> event) {
        System.out.println("[PAYMENT-SERVICE] Received BOOKING_CREATED: " + event);

        Long bookingId = Long.valueOf(event.get("bookingId").toString());
        String movieTitle = event.get("movieTitle").toString();
        Long userId = Long.valueOf(event.get("userId").toString());
        double totalPrice = Double.parseDouble(event.get("totalPrice").toString());

        boolean paymentSuccess = Math.random() > 0.3;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Map<String, Object> resultEvent = new HashMap<>();
        resultEvent.put("bookingId", bookingId);
        resultEvent.put("userId", userId);
        resultEvent.put("movieTitle", movieTitle);
        resultEvent.put("totalPrice", totalPrice);
        resultEvent.put("timestamp", System.currentTimeMillis());

        if (paymentSuccess) {
            resultEvent.put("eventType", "PAYMENT_COMPLETED");
            resultEvent.put("transactionId", "TXN-" + System.currentTimeMillis());
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENT_EXCHANGE,
                    RabbitMQConfig.PAYMENT_COMPLETED_KEY,
                    resultEvent
            );
            System.out.println("[PAYMENT-SERVICE] Published PAYMENT_COMPLETED for booking #" + bookingId);
            updateBookingStatus(bookingId, "CONFIRMED");
        } else {
            resultEvent.put("eventType", "BOOKING_FAILED");
            resultEvent.put("reason", "Thanh toán thất bại — thẻ bị từ chối");
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENT_EXCHANGE,
                    RabbitMQConfig.BOOKING_FAILED_KEY,
                    resultEvent
            );
            System.out.println("[PAYMENT-SERVICE] Published BOOKING_FAILED for booking #" + bookingId);
            updateBookingStatus(bookingId, "FAILED");
        }
    }

    private void updateBookingStatus(Long bookingId, String status) {
        try {
            String bookingServiceUrl = System.getenv().getOrDefault("BOOKING_SERVICE_URL", "http://localhost:8083");
            String url = bookingServiceUrl + "/api/bookings/" + bookingId + "/status?status=" + status;
            restTemplate.put(url, null);
        } catch (Exception e) {
            System.err.println("[PAYMENT-SERVICE] Không thể cập nhật booking status: " + e.getMessage());
        }
    }
}