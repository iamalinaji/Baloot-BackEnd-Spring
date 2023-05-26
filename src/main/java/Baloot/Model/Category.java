package Baloot.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "category")
public class Category {
    @Id
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public Category() {

    }

    public String getName() {
        return name;
    }
}
