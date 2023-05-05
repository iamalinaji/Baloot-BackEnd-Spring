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
import java.util.ArrayList;


@RestController
public class CommodityController {

    @GetMapping("/commodities")
    public ResponseEntity<List<JSONObject>> getAllCommodities() {
        MarketManager market = MarketManager.getInstance();
        List<Commodity> commodities = market.getCommoditiesList();
        List<JSONObject> commodityJsonList = market.commoditiesToJsonList(commodities);
        return ResponseEntity.ok(commodityJsonList);
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
        return ResponseEntity.ok(result);
    }
}

