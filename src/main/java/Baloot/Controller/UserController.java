package Baloot.Controller;

import Baloot.Model.BuyItem;
import Baloot.Service.MarketService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
public class UserController {

    private final MarketService marketService;

    public UserController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUser(@PathVariable("username") String username, HttpServletRequest request) {
        String loggedInUser = request.getAttribute("username").toString();
        if (!Objects.equals(username, loggedInUser)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } else {
            return ResponseEntity.ok(marketService.getUserByUsername(username));
        }
    }

    @GetMapping("/users/{username}/credit")
    public ResponseEntity<?> getUserCredit(@PathVariable("username") String username, HttpServletRequest request) {
        String loggedInUser = request.getAttribute("username").toString();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        return ResponseEntity.ok(marketService.getUserByUsername(username).getCredit());
    }

    @PostMapping("/users/{username}/credit")
    public ResponseEntity<?> addCredit(@PathVariable("username") String username, @RequestBody JSONObject requestBody, HttpServletRequest request) {
        String loggedInUser = request.getAttribute("username").toString();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        try {
            String amount = (String) requestBody.get("amount");
            int creditTobeAdded = Integer.parseInt(amount);
            marketService.addCreditToUser(username, creditTobeAdded);
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
    public ResponseEntity<?> addToCart(@PathVariable("username") String username, @RequestBody JSONObject requestBody, HttpServletRequest request) {
        String loggedInUser = request.getAttribute("username").toString();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        try {
            int commodityId = (int) requestBody.get("commodityId");
            marketService.addToBuyList(username, commodityId);
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
    public ResponseEntity<?> getCarts(@PathVariable("username") String username, HttpServletRequest request) {
        String loggedInUser = request.getAttribute("username").toString();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        List<BuyItem> carts = marketService.getBuyList(username);
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/users/{username}/bought")
    public ResponseEntity<?> getBought(@PathVariable("username") String username, HttpServletRequest request) {
        String loggedInUser = request.getAttribute("username").toString();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        List<BuyItem> bought = marketService.getPurchasedList(username);
        return ResponseEntity.ok(bought);
    }

    @PostMapping("users/{username}/cart/increment/{itemId}")
    public ResponseEntity<?> increaseQuantity(@PathVariable("username") String username, @PathVariable("itemId") int itemId, HttpServletRequest request) {
        String loggedInUser = request.getAttribute("username").toString();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        try {
            marketService.incrementBuyItem(username, itemId);
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
    public ResponseEntity<?> decreaseQuantity(@PathVariable("username") String username, @PathVariable("itemId") int itemId, HttpServletRequest request) {
        String loggedInUser = request.getAttribute("username").toString();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        try {
            marketService.decrementBuyItem(username, itemId);
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
    public ResponseEntity<?> useDiscountCode(@PathVariable("username") String username, @RequestBody JSONObject requestBody, HttpServletRequest request) {
        String loggedInUser = request.getAttribute("username").toString();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        try {
            String discountCode = (String) requestBody.get("discountCode");
            if (marketService.canUserUseDiscount(username, discountCode)) {
                int percent = marketService.getDiscountPercent(discountCode);
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
    public ResponseEntity<?> purchase(@PathVariable("username") String username, @RequestBody JSONObject requestBody, HttpServletRequest request) {
        String loggedInUser = request.getAttribute("username").toString();
        if (!Objects.equals(loggedInUser, username)) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Access denied!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        try {
            String discountCode = (String) requestBody.get("discountCode");
            if (Objects.equals(discountCode, "")) {
                marketService.purchase(username);
            } else {
                marketService.purchase(username, discountCode);
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

