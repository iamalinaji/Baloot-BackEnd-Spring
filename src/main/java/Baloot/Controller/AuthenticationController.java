package Baloot.Controller;

import Baloot.Service.MarketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    private final MarketService marketService;

    public AuthenticationController(MarketService marketService) {
        this.marketService = marketService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        try {
            marketService.login(username, password);
            int cart = marketService.getBuyList(username).size();
            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("cart", cart);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String email = request.get("email");
        String address = request.get("address");
        String birthdate = request.get("birthDate");
        if (marketService.getUserByEmail(email) != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "This email already exists.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (marketService.getUserByUsername(username) != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "This username already exists.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            try {
                marketService.signup(username, password, email, address, birthdate);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "User created successfully.");
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        if (!marketService.isUserLoggedIn()) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "User not logged in");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } else {
            marketService.logout();
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Logout successful");
            return ResponseEntity.ok(responseBody);
        }
    }

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> checkLogin() {
        if (marketService.isUserLoggedIn()) {
            String username = marketService.getLoggedInUser();
            int cart = marketService.getBuyList(username).size();
            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("cart", cart);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User is not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


}
