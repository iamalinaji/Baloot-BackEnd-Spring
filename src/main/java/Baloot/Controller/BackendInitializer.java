package Baloot.Controller;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
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

        SessionFactory sessionFactory;
        // configures settings from hibernate.cfg.xml
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
