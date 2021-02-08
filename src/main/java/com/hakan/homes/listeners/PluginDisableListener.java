package com.hakan.homes.listeners;

import com.hakan.homes.Home;
import com.hakan.homes.HomePlugin;
import com.hakan.homes.PlayerData;
import com.hakan.homes.utils.HomeSettings;
import com.hakan.homes.utils.LocationSerializer;
import com.hakan.homes.utils.yaml.Yaml;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class PluginDisableListener implements Listener {

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(HomePlugin.getInstance())) {
            for (PlayerData playerData : HomeSettings.playerData.values()) {

                Yaml dataFile = playerData.getDataFile();
                for (Home home : playerData.getHomeList().values()) {
                    dataFile.set(home.getHomeName() + ".location", LocationSerializer.locationToString(home.getLocation()));
                }

                dataFile.save();
            }
        }
    }
}