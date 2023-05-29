package Baloot.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name = "commodity")
public class Commodity {
    @Id
    private int id;
    @Column
    private String name;
    @Column
    private int providerId;
    @Column
    private int price;
    @OneToMany(mappedBy = "commodityId", cascade = CascadeType.ALL)
    private List<Rating> ratings;
    @Column
    private float rating;
    @Column
    private int inStock;
    @ManyToMany(targetEntity = Category.class)
    private List<Category> categories;

    @Column(length=1000)
    private String imageUrl;

    public Commodity(int id, String name, int providerId, int price, List<Category> categories, float rating, int inStock, String imageUrl) {
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.price = price;
        this.rating = rating;
        this.inStock = inStock;
        this.categories = categories;
        this.imageUrl = imageUrl;
        ratings = new ArrayList<>();
    }

    public Commodity() {

    }

    public int getId() {
        return id;
    }

    public int getProviderId() {
        return providerId;
    }

    public int getInStock() {
        return inStock;
    }

    public void updateRating() {
        float sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getScore();
        }
        rating = sum / ratings.size();
    }

    public void addRating(int score, String username) {
        for (Rating rating : ratings) {
            if (rating.getUsername().equals(username)) {
                rating.updateScore(score);
                updateRating();
                return;
            }
        }
        Rating rating = new Rating(username, id, score);
        ratings.add(rating);
        updateRating();
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void pickFromStock(int quantity) throws RuntimeException {
        if (inStock == 0) {
            throw new RuntimeException("Not enough in stock");
        }
        inStock-= quantity;
    }
}
