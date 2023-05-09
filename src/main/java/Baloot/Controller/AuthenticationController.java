package Baloot.Controller;

import Baloot.Market.MarketManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        MarketManager market = MarketManager.getInstance();
        try {
            market.login(username,password);
            int cart = market.getBuyList(username).size();
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
        MarketManager market = MarketManager.getInstance();
        if (market.signup(username, password, email, address, birthdate)) {
            int cart = market.getBuyList(username).size();
            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("cart", cart);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Invalid date");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        MarketManager marketManager = MarketManager.getInstance();
        if (!marketManager.isUserLoggedIn()) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "User not logged in");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
        else {
            marketManager.logout();
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Logout successful");
            return ResponseEntity.ok(responseBody);
        }
    }

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> checkLogin() {
        MarketManager market = MarketManager.getInstance();
        if (market.isUserLoggedIn()) {
            String username = market.getLoggedInUser();
            int cart = market.getBuyList(username).size();
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
