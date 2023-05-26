package Baloot.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;

@Entity(name = "comment")
public class Comment {
    @Column
    private String username;
    @Column
    private int commodityId;
    @Id
    private int id;
    @Column
    private String comment;
    @Column
    private Date date;
    @OneToMany(mappedBy = "username")
    private ArrayList<User> upVotes = new ArrayList<>();
    @OneToMany(mappedBy = "username")
    private ArrayList<User> downVotes = new ArrayList<>();
    public Comment(int id, String username, int commodityId, String comment, Date date) {
        this.username = username;
        this.commodityId = commodityId;
        this.id = id;
        this.comment = comment;
        this.date = date;
    }

    public Comment() {

    }

    public void upVote(User username) {
        downVotes.removeIf(user -> user.getUsername().equals(username.getUsername()));
        for (User user : upVotes) {
            if (user.getUsername().equals(username.getUsername())) {
                return;
            }
        }
        upVotes.add(username);
    }

    public void downVote(User username) {
        upVotes.removeIf(user -> user.getUsername().equals(username.getUsername()));
        for (User user : downVotes) {
            if (user.getUsername().equals(username.getUsername())) {
                return;
            }
        }
        downVotes.add(username);
    }

    public void removeVote(User username) {
        for (User user : upVotes) {
            if (user.getUsername().equals(username.getUsername())) {
                upVotes.remove(user);
                return;
            }
        }
        for (User user : downVotes) {
            if (user.getUsername().equals(username.getUsername())) {
                downVotes.remove(user);
                return;
            }
        }
    }

    public int getId() {
        return id;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public Date getDate() {
        return date;
    }

    public int getLikes() {
        return upVotes.size();
    }

    public int getDislikes() {
        return downVotes.size();
    }
}
