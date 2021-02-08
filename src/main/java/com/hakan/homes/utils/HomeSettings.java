package com.hakan.homes.utils;

import com.hakan.homes.HomePlugin;
import com.hakan.homes.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HomeSettings {

    public static Map<String, PlayerData> playerData = new HashMap<>();
    public static Map<String, Boolean> useHomeCommand = new HashMap<>();

    public static String serverVersion;

    public static int getMaxHome(Player player) {
        FileConfiguration config = HomePlugin.config.getFileConfiguration();
        Object maxHome = config.get("settings.max-home." + VaultHook.getPermissions().getPrimaryGroup(player));
        return maxHome != null ? (int) maxHome : config.getInt("settings.max-home.others");
    }

    public static int getTeleportTime(Player player) {
        FileConfiguration config = HomePlugin.config.getFileConfiguration();
        Object teleportTime = config.get("settings.teleport-times." + VaultHook.getPermissions().getPrimaryGroup(player));
        return teleportTime != null ? (int) teleportTime : config.getInt("settings.teleport-times.others");
    }

    public static long getSethomeMoney(Player player) {
        FileConfiguration config = HomePlugin.config.getFileConfiguration();
        Object sethomeMonet = config.get("settings.sethome-money." + VaultHook.getPermissions().getPrimaryGroup(player));
        return sethomeMonet != null ? (int) sethomeMonet : config.getInt("settings.sethome-money.others");
    }
}