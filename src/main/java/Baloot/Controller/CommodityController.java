package Baloot.Controller;

import Baloot.Market.Category;
import Baloot.Market.Commodity;
import Baloot.Market.MarketManager;
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

    @GetMapping("/commodities")
    public ResponseEntity<List<Commodity>> getAllCommodities() {
        MarketManager market = MarketManager.getInstance();
        List<Commodity> commodities = market.getCommoditiesList();
        return ResponseEntity.ok(commodities);
    }

    @GetMapping("/commodities/{commodity_id}")
    public ResponseEntity<Commodity> getCommodityById(@PathVariable("commodity_id") int commodityId) {
        MarketManager market = MarketManager.getInstance();
        Commodity commodity = market.getCommodityById(commodityId);
        if (commodity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commodity);
    }

    @GetMapping("/commodities/{commodity_id}/comments")
    public ResponseEntity<?> getCommentsByCommodityId(@PathVariable int commodity_id) {
        MarketManager market = MarketManager.getInstance();
        try {
            return ResponseEntity.ok(market.getCommentListForCommodityById(commodity_id));
        } catch (Exception e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }

    @PostMapping("/commodities/{commodity_id}/rate")
    public ResponseEntity<?> rateCommodity(@PathVariable int commodity_id, @RequestBody JSONObject requestBody) {
        MarketManager market = MarketManager.getInstance();
        int rate = (int) requestBody.get("rating");
        String username = market.getLoggedInUser();
        try {
            market.rateCommodity(username, commodity_id, rate);
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
        MarketManager market = MarketManager.getInstance();
        List<Commodity> result = market.getCommoditiesByName(name);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/commodities/search-by-category")
    public ResponseEntity<List<Commodity>> searchByCategory(@RequestParam("category") String stringCategory) {
        MarketManager market = MarketManager.getInstance();
        Category category = Category.get(stringCategory);
        List<Commodity> commoditiesByCategory = market.getCommoditiesByCategory(category);
        if (commoditiesByCategory.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(commoditiesByCategory);
    }

    @GetMapping("/commodities/search-by-provider")
    public ResponseEntity<List<Commodity>> searchByProvider(@RequestParam("provider") String provider) {
        MarketManager market = MarketManager.getInstance();
        List<Commodity> commoditiesByProvider = market.getCommoditiesByProvider(provider);
        if (commoditiesByProvider.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(commoditiesByProvider);
    }

    @GetMapping("/commodities/{commodity_id}/suggestions")
    public ResponseEntity<List<Commodity>> suggestCommodities(@PathVariable int commodity_id) {
        MarketManager market = MarketManager.getInstance();
        List<Commodity> suggestedCommodities = market.getSuggestedCommodities(commodity_id);
        if (suggestedCommodities.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(suggestedCommodities);
    }
}

