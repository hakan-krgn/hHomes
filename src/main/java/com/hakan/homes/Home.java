package com.hakan.homes;

import org.bukkit.Location;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Home {

    private String ownerName;
    private Location location;
    private String homeName;

    public Home(String ownerName, Location location, String homeName) {
        this.ownerName = ownerName;
        this.location = location;
        this.homeName = homeName;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getHomeName() {
        return this.homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }
}