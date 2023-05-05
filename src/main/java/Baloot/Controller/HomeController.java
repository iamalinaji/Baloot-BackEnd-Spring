package Baloot.Controller;


import java.util.HashMap;
import java.util.Map;
import Baloot.Market.MarketManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        MarketManager market = MarketManager.getInstance();
        Map<String, String> response = new HashMap<>();
        String username = isLoggedIn() ? market.getLoggedInUser() : "guest";
        response.put("username", username);
        return response;
    }

    private boolean isLoggedIn() {
        MarketManager market = MarketManager.getInstance();
        return market.isUserLoggedIn();
    }

}
