package iuh.fit.se.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQConfig {
    // Exchanges
    public static final String BOOKING_EXCHANGE = "booking.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";

    // Routing keys
    public static final String BOOKING_CREATED_KEY = "booking.created";
    public static final String PAYMENT_COMPLETED_KEY = "payment.completed";
    public static final String BOOKING_FAILED_KEY = "booking.failed";

    // Queues
    public static final String BOOKING_CREATED_QUEUE = "booking.created.queue";
    public static final String PAYMENT_COMPLETED_QUEUE = "payment.completed.queue";
    public static final String BOOKING_FAILED_QUEUE = "booking.failed.queue";

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(BOOKING_EXCHANGE);
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue bookingCreatedQueue() {
        return new Queue(BOOKING_CREATED_QUEUE, true);
    }

    @Bean
    public Queue paymentCompletedQueue() {
        return new Queue(PAYMENT_COMPLETED_QUEUE, true);
    }

    @Bean
    public Queue bookingFailedQueue() {
        return new Queue(BOOKING_FAILED_QUEUE, true);
    }

    @Bean
    public Binding bookingCreatedBinding() {
        return BindingBuilder.bind(bookingCreatedQueue())
                .to(bookingExchange())
                .with(BOOKING_CREATED_KEY);
    }

    @Bean
    public Binding paymentCompletedBinding() {
        return BindingBuilder.bind(paymentCompletedQueue())
                .to(paymentExchange())
                .with(PAYMENT_COMPLETED_KEY);
    }

    @Bean
    public Binding bookingFailedBinding() {
        return BindingBuilder.bind(bookingFailedQueue())
                .to(paymentExchange())
                .with(BOOKING_FAILED_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}