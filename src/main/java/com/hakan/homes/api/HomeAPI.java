package com.hakan.homes.api;

import com.hakan.homes.Home;
import com.hakan.homes.HomePlugin;
import com.hakan.homes.PlayerData;
import com.hakan.homes.utils.HomeSettings;
import com.hakan.homes.utils.LocationSerializer;
import com.hakan.homes.utils.Utils;
import com.hakan.homes.utils.yaml.Yaml;
import com.hakan.messageplugin.api.MessageAPI;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class HomeAPI {

    private HomeAPI() {
    }

    public static HomeAPI getInstance() {
        return new HomeAPI();
    }

    public PlayerData loadPlayerData(String playerName) {
        Map<String, Home> homes = new HashMap<>();

        Yaml data = new Yaml(HomePlugin.getInstance().getDataFolder() + "/data/" + playerName + ".yml");

        for (String key : data.getConfigurationSection("").getKeys(false)) {
            String locationAsString = data.getString(key + ".location");

            Home home = new Home(playerName, LocationSerializer.stringToLocation(locationAsString), key);
            homes.put(key, home);
        }

        PlayerData playerData = new PlayerData(playerName, homes, data);
        HomeSettings.playerData.put(playerName, playerData);

        return playerData;
    }

    public PlayerData getPlayerData(String playerName) {
        PlayerData playerData = HomeSettings.playerData.get(playerName);
        return playerData != null ? playerData : loadPlayerData(playerName);
    }

    @Nullable
    public Map<String, Home> getPlayerHomes(String playerName) {
        PlayerData playerData = getPlayerData(playerName);
        return playerData != null ? playerData.getHomeList() : new HashMap<>();
    }

    public Home getHome(String playerName, String homeName) {
        PlayerData playerData = getPlayerData(playerName);
        return playerData != null ? playerData.getHomeList().get(homeName) : null;
    }

    public void setHome(String playerName, Location location, String homeName) {
        Home home = new Home(playerName, location, homeName);
        PlayerData playerData = getPlayerData(playerName);
        if (playerData != null) {
            playerData.addHome(home);
        }
    }

    public void deleteHome(String playerName, Home home) {
        PlayerData playerData = getPlayerData(playerName);
        if (playerData != null) {
            playerData.removeHome(home);
            playerData.getDataFile().set(home.getHomeName(), null);
        }
    }

    public void deleteHome(String playerName, String homeName) {
        PlayerData playerData = getPlayerData(playerName);
        if (playerData != null) deleteHome(playerName, playerData.getHome(homeName));
    }

    public void teleport(Player player, Home home, Callback callback) {
        if (player == null || home == null) {
            callback.confirm(false);
            return;
        }

        FileConfiguration config = HomePlugin.config.getFileConfiguration();

        int teleportTime = HomeSettings.getTeleportTime(player);

        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        new BukkitRunnable() {
            int counter = 0;

            public void run() {
                if (x != player.getLocation().getBlockX() || y != player.getLocation().getBlockY() || z != player.getLocation().getBlockZ()) {
                    String title = Utils.getText(config, "settings.teleport-cancel-title");
                    String subtitle = Utils.getText(config, "settings.teleport-cancel-subtitle");
                    MessageAPI.getTitleAPI().getTitleManager().setTitle(title).setSubtitle(subtitle).setFadeIn(0).setFadeOut(5).setStay(60).create().send(player);
                    callback.confirm(false);
                    cancel();
                    return;
                }
                if (counter == teleportTime) {
                    String title = Utils.getText(config, "settings.teleported-title").replace("%name%", home.getHomeName());
                    String subtitle = Utils.getText(config, "settings.teleported-subtitle").replace("%name%", home.getHomeName());
                    MessageAPI.getTitleAPI().getTitleManager().setTitle(title).setSubtitle(subtitle).setFadeIn(0).setFadeOut(5).setStay(60).create().send(player);
                    callback.confirm(true);
                    cancel();
                    return;
                } else {
                    String title = Utils.getText(config, "settings.teleporting-title").replace("%sec%", teleportTime - counter + "");
                    String subtitle = Utils.getText(config, "settings.teleporting-subtitle").replace("%sec%", teleportTime - counter + "");
                    MessageAPI.getTitleAPI().getTitleManager().setTitle(title).setSubtitle(subtitle).setFadeIn(0).setFadeOut(0).setStay(60).create().send(player);
                }
                counter++;
            }
        }.runTaskTimer(HomePlugin.getInstance(), 0, 20);

    }

    public void teleport(Player player, String homeName, Callback callback) {
        teleport(player, getHome(player.getName(), homeName), callback);
    }

    public interface Callback {
        void confirm(boolean isTeleported);
    }
}