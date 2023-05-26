package Baloot.Model;

import java.util.ArrayList;

public class Discount {
    private final String code;
    private final int percent;

    private final ArrayList<String> usedUsers = new ArrayList<>();

    public Discount(String code, int percent) {
        this.code = code;
        this.percent = percent;
    }

    public String getCode() {
        return code;
    }

    public int getPercent() {
        return percent;
    }

    public boolean cantUse(String username) {
        for (String usedUser : usedUsers) {
            if (usedUser.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void use(String username) throws RuntimeException {
        for (String usedUser : usedUsers) {
            if (usedUser.equals(username)) {
                throw new RuntimeException("User already used this discount");
            }
        }
        usedUsers.add(username);
    }

}
