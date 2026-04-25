package iuh.fit.se.service;

import iuh.fit.se.config.RabbitMQConfig;
import iuh.fit.se.dto.LoginRequest;
import iuh.fit.se.dto.RegisterRequest;
import iuh.fit.se.model.User;
import iuh.fit.se.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // production: hash password
        user.setEmail(request.getEmail());
        User saved = userRepository.save(user);

        // Publish event USER_REGISTERED
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_REGISTERED");
        event.put("userId", saved.getId());
        event.put("username", saved.getUsername());
        event.put("email", saved.getEmail());
        event.put("timestamp", System.currentTimeMillis());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EXCHANGE,
                RabbitMQConfig.USER_REGISTERED_ROUTING_KEY,
                event
        );
        System.out.println("[USER-SERVICE] Published USER_REGISTERED for: " + saved.getUsername());
        return saved;
    }

    public Optional<User> login(LoginRequest request) {
        return userRepository.findByUsernameAndPassword(
                request.getUsername(), request.getPassword()
        );
    }
}
