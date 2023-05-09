package Baloot.Market;

public class BuyItem {
    private final Commodity commodity;
    public int quantity;

    BuyItem(Commodity commodity, int quantity) {
        this.commodity = commodity;
        this.quantity = quantity;
    }
    public Commodity getCommodity() {
        return commodity;
    }

    public int getQuantity() {
        return quantity;
    }
}
