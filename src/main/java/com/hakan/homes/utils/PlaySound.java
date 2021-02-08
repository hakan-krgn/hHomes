package com.hakan.homes.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlaySound {

    public static void playVillagerNo(Player player) {
        if (HomeSettings.serverVersion.equals("v1_8_R3")) {
            player.playSound(player.getLocation(), Sound.valueOf("VILLAGER_NO"), 1, 1);
        } else {
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_VILLAGER_NO"), 1, 1);
        }
    }

    public static void playButtonClick(Player player) {
        if (HomeSettings.serverVersion.equals("v1_8_R3")) {
            player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1, 1);
        } else {
            player.playSound(player.getLocation(), Sound.valueOf("UI_BUTTON_CLICK"), 1, 1);
        }
    }

    public static void playExperienceOrb(Player player) {
        if (HomeSettings.serverVersion.equals("v1_8_R3")) {
            player.playSound(player.getLocation(), Sound.valueOf("ORB_PICKUP"), 1, 1);
        } else {
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_EXPERIENCE_ORB_PICKUP"), 1, 1);
        }
    }
}