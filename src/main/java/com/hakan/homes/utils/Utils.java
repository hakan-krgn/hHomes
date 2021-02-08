package com.hakan.homes.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

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
}