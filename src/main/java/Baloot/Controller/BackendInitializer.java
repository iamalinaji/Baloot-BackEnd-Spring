package Baloot.Controller;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import Baloot.Market.MarketManager;

@Component
public class BackendInitializer {

    @PostConstruct
    public void init() {
        MarketManager marketManager = MarketManager.getInstance();
        marketManager.init();
        System.out.println("MarketManager initialized");
    }
}