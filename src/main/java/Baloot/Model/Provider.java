package Baloot.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;

@Entity(name = "provider")
public class Provider {
    @Id
    private int id;
    @Column
    private String name;
    @Column
    private Date registryDate;
    @Column
    private String imageUrl;

    public Provider(int id, String name, Date registryDate, String imageUrl) {
        this.id = id;
        this.name = name;
        this.registryDate = registryDate;
        this.imageUrl = imageUrl;
    }

    public Provider() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getRegistryDate() {
        return registryDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
