package Baloot.Model;

import jakarta.persistence.*;

import java.util.*;

@Entity(name = "user")
public class User {
    @Id
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column(name = "birthday")
    private Date birthDay;

    @Column
    private String address;

    @Column
    private int credit;

    @ManyToMany
    private final List<BuyItem> buyList = new ArrayList<>();

    @ManyToMany
    private final List<BuyItem> purchasedList = new ArrayList<>();

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

    // Getters and setters
    // ...

    public void updateUser(String password, String email, Date birthDay, String address, int credit) {
        this.password = password;
        this.email = email;
        this.birthDay = birthDay;
        this.address = address;
        this.credit = credit;
    }

    public void purchase(int price) {
        if (buyList.isEmpty()) {
            throw new RuntimeException("Buy list is empty");
        }
        if (credit < price) {
            throw new RuntimeException("Not enough credit");
        }
        credit -= price;
        purchasedList.addAll(buyList);
        buyList.clear();
    }

    public void addToBuyList(Commodity commodity) {
        for (BuyItem buyListItem : buyList) {
            if (buyListItem.getCommodity().getId() == commodity.getId()) {
                buyListItem.setQuantity(buyListItem.getQuantity() + 1);
                return;
            }
        }
        BuyItem toBeAdded = new BuyItem(commodity, this, 1);
        buyList.add(toBeAdded);
    }

    public void removeFromBuyList(int commodityId) {
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

    public List<BuyItem> getBuyList() {
        return Collections.unmodifiableList(buyList);
    }

    public List<BuyItem> getPurchasedList() {
        return Collections.unmodifiableList(purchasedList);
    }

    public String getUsername() {
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
