package Baloot.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;

@Entity(name = "user")
public class User {
    @Id
    private String username;

    @Column
    @JsonIgnore
    private String password;

    @Column
    private String email;

    @Column(name = "birthday")
    private Date birthDay;

    @Column
    private String address;

    @Column
    private int credit;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<BuyItem> buyList = new ArrayList<>();

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

    public void addToBuyList(Commodity commodity) {
        for (BuyItem buyListItem : buyList) {
            if (buyListItem.getCommodity().getId() == commodity.getId() && !buyListItem.isPurchased()) {
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
            if (buyItem.getCommodity().getId() == commodityId && !buyItem.isPurchased()) {
                iterator.remove();
                return;
            }
        }
        throw new RuntimeException("Item not found");
    }

    public List<BuyItem> getBuyList() {
        return buyList.stream().filter(buyItem -> !buyItem.isPurchased()).toList();
    }

    public List<BuyItem> getPurchasedList() {
        return buyList.stream().filter(BuyItem::isPurchased).toList();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password,this.password);
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

    public void setCredit(int i) {
        this.credit = i;
    }
}
