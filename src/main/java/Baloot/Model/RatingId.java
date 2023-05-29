package Baloot.Model;

import java.io.Serializable;
import java.util.Objects;

public class RatingId implements Serializable {
    private String username;
    private int commodityId;

    public RatingId() {
    }

    public RatingId(String username, int commodityId) {
        this.username = username;
        this.commodityId = commodityId;
    }

    // Implement equals() and hashCode() methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RatingId that)) return false;
        return username.equals(that.username) &&
                commodityId == that.commodityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, commodityId);
    }


}
