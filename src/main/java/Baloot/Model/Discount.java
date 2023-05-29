package Baloot.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "discount")
public class Discount {
    @Id
    private String code;
    private int percent;

    @ManyToMany
    private final List<User> usedUsers = new ArrayList<>();

    public Discount(String code, int percent) {
        this.code = code;
        this.percent = percent;
    }

    public Discount() {
    }

    public String getCode() {
        return code;
    }

    public int getPercent() {
        return percent;
    }

    public boolean cantUse(String username) {
        for (User usedUser : usedUsers) {
            if (usedUser.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void use(User user) throws RuntimeException {
        for (User usedUser : usedUsers) {
            if (usedUser.getUsername().equals(user.getUsername())) {
                throw new RuntimeException("User already used this discount");
            }
        }
        usedUsers.add(user);
    }

}
