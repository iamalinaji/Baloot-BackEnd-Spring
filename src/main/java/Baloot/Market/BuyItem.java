package Baloot.Market;

import Baloot.Market.Commodity;
import java.util.ArrayList;
public class BuyItem {
    private final int commodityId;
    public int quantity;

    BuyItem(int commodityId, int quantity) {
        this.commodityId = commodityId;
        this.quantity = quantity;
    }
    public int getCommodityId(){
        return commodityId;
    }
}
