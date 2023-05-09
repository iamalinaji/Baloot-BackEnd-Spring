package Baloot.Controller;

import Baloot.Market.BuyItem;
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
        try {
            String amount = (String) requestBody.get("amount");
            int creditTobeAdded = Integer.parseInt(amount);
            market.addCreditToUser(username, creditTobeAdded);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Credit added successfully");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @PostMapping("/users/{username}/cart")
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

    @GetMapping("/users/{username}/cart")
    public ResponseEntity<?> getCarts(@PathVariable("username") String username) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        List<BuyItem> carts = market.getBuyList(username);
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
        List<BuyItem> bought = market.getPurchasedList(username);
        return ResponseEntity.ok(bought);
    }

    @PostMapping("users/{username}/cart/increment/{itemId}")
    public ResponseEntity<?> increaseQuantity(@PathVariable("username") String username, @PathVariable("itemId") int itemId) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        try {
            market.incrementBuyItem(username, itemId);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Commodity Incremented in buy list successfully.");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @PostMapping("users/{username}/cart/decrement/{itemId}")
    public ResponseEntity<?> decreaseQuantity(@PathVariable("username") String username, @PathVariable("itemId") int itemId) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        try {
            market.decrementBuyItem(username, itemId);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Commodity Decremented in buy list successfully.");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @PostMapping("users/{username}/discount")
    public ResponseEntity<?> useDiscountCode(@PathVariable("username") String username, @RequestBody JSONObject requestBody) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        try {
            String discountCode = (String) requestBody.get("discountCode");
            if (market.canUserUseDiscount(username, discountCode)) {
                int percent = market.getDiscountPercent(discountCode);
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("percent", percent);
                return ResponseEntity.ok(responseBody);
            }
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Discount code is not valid!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }



    @PostMapping("users/{username}/cart/purchase")
    public ResponseEntity<?> purchase(@PathVariable("username") String username, @RequestBody JSONObject requestBody) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        try {
            String discountCode = (String) requestBody.get("discountCode");
            if (discountCode == null) {
                market.purchase(username);
            } else {
                market.purchase(username, discountCode);
            }
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Purchase done successfully.");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

}

