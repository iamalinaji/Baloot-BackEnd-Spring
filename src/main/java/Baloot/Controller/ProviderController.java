package Baloot.Controller;

import Baloot.Market.MarketManager;
import Baloot.Market.Provider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ProviderController {

    @GetMapping("/providers/{provider_id}")
    public ResponseEntity<?> getProviderById(@PathVariable("provider_id") int providerId) {
        MarketManager market = MarketManager.getInstance();
        Provider provider = market.getProviderById(providerId);
        try {
            return ResponseEntity.ok(provider);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @GetMapping("/providers/{provider_id}/commodities")
    public ResponseEntity<?> getCommoditiesByProviderId(@PathVariable("provider_id") int providerId) {
        MarketManager market = MarketManager.getInstance();
        try {
            market.getCommoditiesByProvider(providerId);
            return ResponseEntity.ok(market.getCommoditiesByProvider(providerId));
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }
}

