package Baloot.Controller;

import Baloot.Model.Commodity;
import Baloot.Service.MarketService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CommodityController {

    private final MarketService marketService;

    public CommodityController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/commodities")
    public ResponseEntity<List<Commodity>> getAllCommodities() {
        List<Commodity> commodities = marketService.getCommoditiesList();
        return ResponseEntity.ok(commodities);
    }

    @GetMapping("/commodities/{commodity_id}")
    public ResponseEntity<Commodity> getCommodityById(@PathVariable("commodity_id") int commodityId) {
        Commodity commodity = marketService.getCommodityById(commodityId);
        if (commodity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commodity);
    }

    @GetMapping("/commodities/{commodity_id}/comments")
    public ResponseEntity<?> getCommentsByCommodityId(@PathVariable int commodity_id) {
        try {
            return ResponseEntity.ok(marketService.getCommentListForCommodityById(commodity_id));
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @PostMapping("/commodities/{commodity_id}/rate")
    public ResponseEntity<?> rateCommodity(@PathVariable int commodity_id, @RequestBody JSONObject requestBody) {
        int rate = (int) requestBody.get("rating");
        String username = marketService.getLoggedInUser();
        try {
            marketService.rateCommodity(username, commodity_id, rate);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Commodity rated successfully");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @GetMapping("/commodities/search-by-name")
    public ResponseEntity<List<Commodity>> searchByName(@RequestParam("name") String name) {
        List<Commodity> result = marketService.getCommoditiesByName(name);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/commodities/search-by-category")
    public ResponseEntity<List<Commodity>> searchByCategory(@RequestParam("category") String stringCategory) {
        List<Commodity> commoditiesByCategory = marketService.getCommoditiesByCategory(stringCategory);
        if (commoditiesByCategory.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(commoditiesByCategory);
    }

    @GetMapping("/commodities/search-by-provider")
    public ResponseEntity<List<Commodity>> searchByProvider(@RequestParam("provider") String provider) {
        List<Commodity> commoditiesByProvider = marketService.getCommoditiesByProvider(provider);
        if (commoditiesByProvider.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(commoditiesByProvider);
    }

    @GetMapping("/commodities/{commodity_id}/suggestions")
    public ResponseEntity<List<Commodity>> suggestCommodities(@PathVariable int commodity_id) {
        List<Commodity> suggestedCommodities = marketService.getSuggestedCommodities(commodity_id);
        if (suggestedCommodities.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(suggestedCommodities);
    }
}

