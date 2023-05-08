package Baloot.Controller;

import Baloot.Market.Commodity;
import Baloot.Market.MarketManager;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class userController {

    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        MarketManager market = MarketManager.getInstance();
        if (!market.isUserLoggedIn()) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "User not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        } else if (!Objects.equals(username, market.getLoggedInUser())) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } else {
            return ResponseEntity.ok(market.getUserByUsername(username));
        }
    }

    @GetMapping("/users/{username}/credit")
    public ResponseEntity<?> getUserCredit(@PathVariable("username") String username) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        return ResponseEntity.ok(market.getUserByUsername(username).getCredit());
    }

    @PostMapping("/users/{username}/credit")
    public ResponseEntity<?> addCredit(@PathVariable("username") String username, @RequestBody JSONObject requestBody) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        int amount = (int) requestBody.get("amount");
        market.addCreditToUser(username, amount);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Credit added successfully");
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/users/{username}/carts")
    public ResponseEntity<?> addToCart(@PathVariable("username") String username, @RequestBody JSONObject requestBody) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        try {
            int commodityId = (int) requestBody.get("commodityId");
            market.addToBuyList(username, commodityId);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Commodity added to cart successfully");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @GetMapping("/users/{username}/carts")
    public ResponseEntity<?> getCarts(@PathVariable("username") String username) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        List<Commodity> carts = market.getBuyList(username);
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/users/{username}/bought")
    public ResponseEntity<?> getBought(@PathVariable("username") String username) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        List<Commodity> bought = market.getPurchasedList(username);
        return ResponseEntity.ok(bought);
    }
}

