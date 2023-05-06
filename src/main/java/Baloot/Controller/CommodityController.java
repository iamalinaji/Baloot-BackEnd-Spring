package Baloot.Controller;

import Baloot.Market.Category;
import Baloot.Market.Commodity;
import Baloot.Market.MarketManager;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
public class CommodityController {

    @GetMapping("/commodities")
    public ResponseEntity<List<JSONObject>> getAllCommodities() {
        MarketManager market = MarketManager.getInstance();
        List<Commodity> commodities = market.getCommoditiesList();
        List<JSONObject> commodityJsonList = market.commoditiesToJsonList(commodities);
        return ResponseEntity.ok(commodityJsonList);
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

    @GetMapping("/commodities/search-by-name")
    public ResponseEntity<List<JSONObject>> searchByName(@RequestParam String name) {
        MarketManager market = MarketManager.getInstance();
        List<Commodity> result = market.getCommoditiesByName(name);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<JSONObject> commodityJsonList = market.commoditiesToJsonList(result);
            return ResponseEntity.ok(commodityJsonList);
        }
    }

    @GetMapping("/commodities/search-by-category")
    public ResponseEntity<List<JSONObject>> searchByCategory(@RequestParam String stringCategory) {
        MarketManager market = MarketManager.getInstance();
        Category category = Category.get(stringCategory);
        List <Commodity> commoditiesByCategory = market.getCommoditiesByCategory(category);
        List<JSONObject> result = market.commoditiesToJsonList(commoditiesByCategory);
        if (result.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }
    @GetMapping("/commodities/search-by-price-range")
    public ResponseEntity<List<JSONObject>> searchByPriceRange(@RequestParam int minPrice, @RequestParam int maxPrice) {
        MarketManager market = MarketManager.getInstance();
        List <Commodity> commodities = market.getCommoditiesWithinPrice(minPrice,maxPrice);
        List<JSONObject> result = market.commoditiesToJsonList(commodities);
        if (result.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }
}

