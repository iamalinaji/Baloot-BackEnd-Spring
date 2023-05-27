package Baloot.Market;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "rating")
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
}
