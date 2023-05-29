package Baloot.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "buy_item")
@IdClass(BuyItemId.class)
public class BuyItem {
    @Id
    @ManyToOne()
    private Commodity commodity;

    @Id
    @JsonIgnore
    @ManyToOne()
    private User user;

    @Column
    private int quantity;

    @Column
    private boolean isPurchased;

    public BuyItem(Commodity commodity, User user, int quantity) {
        this.commodity = commodity;
        this.user = user;
        this.quantity = quantity;
        this.isPurchased = false;
    }

    public BuyItem() {
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Define equals() and hashCode() methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuyItem buyItem = (BuyItem) o;
        return Objects.equals(commodity, buyItem.commodity) &&
                Objects.equals(user, buyItem.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commodity, user);
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }
}
