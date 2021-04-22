package com.hakan.homes.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Utils {

    public static List<String> replaceList(List<String> list, String str1, String str2) {
        List<String> newList = new ArrayList<>();
        for (String line : list) {
            newList.add(line.replace(str1, str2));
        }
        return newList;
    }

    public static String getText(FileConfiguration fileConfiguration, String from) {
        return ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString(from));
    }

    public static long getMaximum(Player player, String perm, int def) {
        TreeSet<Integer> permMax = new TreeSet<>();
        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
            String permission = permissionAttachmentInfo.getPermission();
            if (permission.contains(perm)) {
                permMax.add(Integer.parseInt(permission.replace(perm, "")));
            }
        }
        return permMax.size() != 0 ? permMax.last() : def;
    }
}