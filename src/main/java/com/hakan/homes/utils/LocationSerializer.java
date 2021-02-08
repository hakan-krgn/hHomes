package com.hakan.homes.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer {

    public static String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    public static Location stringToLocation(String stringLoc) {
        String[] splitLoc = stringLoc.split(",");
        return new Location(Bukkit.getWorld(splitLoc[0]), Double.parseDouble(splitLoc[1]), Double.parseDouble(splitLoc[2]), Double.parseDouble(splitLoc[3]));
    }
}