package com.hakan.homes.utils;

import com.hakan.homes.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HomeSettings {

    public static Map<String, PlayerData> playerData = new HashMap<>();
    public static Map<String, Boolean> useHomeCommand = new HashMap<>();

    public static String serverVersion;

    public static long getMaxHome(Player player) {
        return Utils.getMaximum(player, "home.maxhome.", 1);
    }

    public static long getTeleportTime(Player player) {
        return Utils.getMaximum(player, "home.teleport.", 3);
    }

    public static long getSethomeMoney(Player player) {
        return Utils.getMaximum(player, "home.money.", 1000);
    }
}