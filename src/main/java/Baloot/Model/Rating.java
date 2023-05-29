package Baloot.Model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "rating")
@IdClass(RatingId.class)
public class Rating {
    @Id
    private String username;

    @Id
    private int commodityId;

    @Column
    private int score;

    public Rating(String username, int commodityId, int score) {
        this.username = username;
        this.commodityId = commodityId;
        this.score = score;
    }

    public Rating() {

    }

    public void updateScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
    	this.score = score;
    }

    public void setUsername(String username) {
    	this.username = username;
    }

    public void setCommodityId(int commodityId) {
    	this.commodityId = commodityId;
    }

    @Override
    public boolean equals(Object obj) {
    	if (obj == null) {
    		return false;
    	}
    	if (obj == this) {
    		return true;
    	}
    	if (!(obj instanceof Rating rating)) {
    		return false;
    	}
        return rating.getUsername().equals(username) && rating.getCommodityId() == commodityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, commodityId);
    }
}
