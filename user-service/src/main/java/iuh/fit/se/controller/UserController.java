package iuh.fit.se.controller;

import iuh.fit.se.dto.LoginRequest;
import iuh.fit.se.dto.RegisterRequest;
import iuh.fit.se.model.User;
import iuh.fit.se.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            return ResponseEntity.ok(Map.of(
                    "message", "Đăng ký thành công",
                    "userId", user.getId(),
                    "username", user.getUsername()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.login(request)
                .map(user -> ResponseEntity.ok(Map.of(
                        "message", "Đăng nhập thành công",
                        "userId", user.getId(),
                        "username", user.getUsername()
                )))
                .orElse(ResponseEntity.status(401)
                        .body(Map.of("error", "Sai tên đăng nhập hoặc mật khẩu")));
    }
}
