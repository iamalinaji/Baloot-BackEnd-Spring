package Baloot.Model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BuyItemId implements Serializable {
    private Commodity commodity;
    private User user;

    public BuyItemId() {
    }

    public BuyItemId(Commodity commodity, User user) {
        this.commodity = commodity;
        this.user = user;
    }

    // Implement equals() and hashCode() methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuyItemId that = (BuyItemId) o;
        return Objects.equals(commodity, that.commodity) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commodity, user);
    }
}
