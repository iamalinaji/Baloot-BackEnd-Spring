package Baloot.Market;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Optional;

@Entity(name = "category")
public enum Category {
    Vegetables("Vegetables"),
    Technology("Technology"),
    Phone("Phone"),
    PhoneAccessory("Phone Accessory"),
    Fruits("Fruits"),
    Unknown("Unknown");

    @Id
    private String val;

    Category(String s) {
        this.val = s;
    }

    Category() {

    }

    public static Category get(String url) {
        Optional<Category> c = Arrays.stream(Category.values())
                .filter(env -> env.val.contains(url))
                .findFirst();

        return c.orElse(Unknown);
    }
    public Category getCategoryFromString(String categoryString) {
        for (Category category : Category.values()) {
            if (category.val.equalsIgnoreCase(categoryString)) {
                return category;
            }
        }
        return Category.Unknown;
    }
}
