package Baloot.Controller;


import Baloot.Market.MarketManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LogoutController {

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        MarketManager marketManager = MarketManager.getInstance();

        if (!marketManager.isUserLoggedIn()) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", 401);
            responseBody.put("message", "User not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }

        marketManager.logout();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 200);
        responseBody.put("message", "Logout successful");
        return ResponseEntity.ok(responseBody);
    }
}

