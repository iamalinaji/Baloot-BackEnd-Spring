package Baloot.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import Baloot.Market.MarketManager;
@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        // Check username and password and perform login logic here
        boolean loggedIn = performLogin(username, password);

        if (loggedIn) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            return ResponseEntity.ok().body(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkLogin(HttpServletRequest request) {
        if (isLoggedIn(request)) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            return ResponseEntity.ok().body(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "User is not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        MarketManager marketManager = MarketManager.getInstance();
        return marketManager.isUserLoggedIn();
    }

    private boolean performLogin(String username, String password) {
        MarketManager marketManager = MarketManager.getInstance();
        return  marketManager.login(username, password);
    }
}
