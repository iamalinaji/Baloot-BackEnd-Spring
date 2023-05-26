package Baloot.Market;

import javax.persistence.*;
import java.util.*;

@Entity(name = "User")
public class User {
    @Id
    private String username;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private Date birthDay;
    @Column
    private String address;
    @Column
    private int credit;
    @ManyToMany(mappedBy = "commodity")
    private final ArrayList<BuyItem> buyList = new ArrayList<>();
    @ManyToMany(mappedBy = "commodity")
    private final ArrayList<BuyItem> purchasedList = new ArrayList<>();

    public User(String username, String password, String email, Date birthDay, String address, int credit) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDay = birthDay;
        this.address = address;
        this.credit = credit;
    }

    public User() {

    }

    void updateUser(String password, String email, Date birthDay, String address, int credit) {
        this.password = password;
        this.email = email;
        this.birthDay = birthDay;
        this.address = address;
        this.credit = credit;
    }

    public void purchase(int price) throws RuntimeException {
        if (buyList.size() == 0) {
            throw new RuntimeException("Buy list is empty");
        }
        if (credit < price) {
            throw new RuntimeException("Not enough credit");
        }
        credit -= price;
        purchasedList.addAll(buyList);
        buyList.clear();
    }

    public void addToBuyList(Commodity commodity) throws RuntimeException {
        for (BuyItem buyListItem : buyList) {
            if (buyListItem.getCommodity().getId() == commodity.getId()) {
                buyListItem.setQuantity(buyListItem.getQuantity() + 1);
                return;
            }
        }
        BuyItem toBeAdded = new BuyItem(commodity,1);
        buyList.add(toBeAdded);
    }

    public void removeFromBuyList(int commodityId) throws RuntimeException {
        Iterator<BuyItem> iterator = buyList.iterator();
        while (iterator.hasNext()) {
            BuyItem buyItem = iterator.next();
            if (buyItem.getCommodity().getId() == commodityId) {
                iterator.remove();
                return;
            }
        }
        throw new RuntimeException("Item not found");
    }

    List<BuyItem> getBuyList() {
        return Collections.unmodifiableList(buyList);
    }

    List<BuyItem> getPurchasedList() {
        return Collections.unmodifiableList(purchasedList);
    }

    String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public String getAddress() {
        return address;
    }

    public int getCredit() {
        return credit;
    }

    public void addCredit(int credit) {
        this.credit += credit;
    }
}
