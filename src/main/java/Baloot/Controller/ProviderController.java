package Baloot.Controller;

import Baloot.Model.Provider;
import Baloot.Service.MarketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class ProviderController {

    private final MarketService marketService;

    public ProviderController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/providers/{provider_id}")
    public ResponseEntity<?> getProviderById(@PathVariable("provider_id") int providerId) {
        Provider provider = marketService.getProviderById(providerId);
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
        try {
            marketService.getCommoditiesByProvider(providerId);
            return ResponseEntity.ok(marketService.getCommoditiesByProvider(providerId));
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }
}

