package Baloot.Market;

import java.util.Date;

public class Provider {
    private final int id;
    private final String name;
    private final Date registryDate;
    private final String imageUrl;

    public Provider(int id, String name, Date registryDate, String imageUrl) {
        this.id = id;
        this.name = name;
        this.registryDate = registryDate;
        this.imageUrl = imageUrl;
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
