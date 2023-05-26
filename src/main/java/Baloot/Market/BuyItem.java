package Baloot.Market;

import javax.persistence.*;

@Entity(name = "buy-item")
public class BuyItem {
    @Id
    @ManyToOne
    private Commodity commodity;
    @Column
    private int quantity;

    BuyItem(Commodity commodity, int quantity) {
        this.commodity = commodity;
        this.quantity = quantity;
    }

    public BuyItem() {

    }

    public Commodity getCommodity() {
        return commodity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
