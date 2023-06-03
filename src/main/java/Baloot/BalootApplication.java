package Baloot;

import Baloot.Service.MarketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication
@EntityScan("Baloot.Model")
@EnableJpaRepositories("Baloot.Repository")
public class BalootApplication implements CommandLineRunner {

    private final MarketService marketService;

    public BalootApplication(MarketService marketService) {
        this.marketService = marketService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BalootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--------------------------");
        marketService.init();
        System.out.println("MarketManager initialized");
    }
}
