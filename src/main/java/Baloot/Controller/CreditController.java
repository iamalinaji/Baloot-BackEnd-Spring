package Baloot.Controller;


import Baloot.Market.MarketManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;


@RestController
@RequestMapping("/credit")
public class CreditController {

    @GetMapping
    public ResponseEntity<?> getUserCredit(@RequestParam String username) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser, username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(market.getUserByUsername(username).getCredit());
    }

    @PostMapping
    public ResponseEntity<?> addCredit(@RequestParam String username, @RequestParam int amount) {
        MarketManager market = MarketManager.getInstance();
        String loggedInUser = market.getLoggedInUser();
        if (!Objects.equals(loggedInUser,username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        market.addCreditToUser(username, amount);
        return ResponseEntity.ok("Credit added successfully");
    }

}
