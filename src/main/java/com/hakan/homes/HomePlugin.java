package com.hakan.homes;

import com.hakan.homes.cmd.HomeCommand;
import com.hakan.homes.listeners.HClaimsProtectListeners;
import com.hakan.homes.listeners.PluginDisableListener;
import com.hakan.homes.utils.HomeSettings;
import com.hakan.homes.utils.Metrics;
import com.hakan.homes.utils.VaultHook;
import com.hakan.homes.utils.yaml.Yaml;
import com.hakan.icreator.ItemCreator;
import com.hakan.invapi.InventoryAPI;
import com.hakan.messageplugin.api.MessageAPI;
import com.hakan.signapi.SignAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HomePlugin extends JavaPlugin {

    public static Yaml config;

    private static Plugin instance;

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getLogger().info("--------------------------------");
        Bukkit.getLogger().info("                                ");
        Bukkit.getLogger().info("  hHomes activated. Good uses!  ");
        Bukkit.getLogger().info("                                ");
        Bukkit.getLogger().info("--------------------------------");

        new Metrics(this, 10266);
        new VaultHook().setupEconomy().setupPermissions();
        InventoryAPI.setup(this);
        MessageAPI.setup(this);
        SignAPI.setup(this);
        ItemCreator.setup(this, "type", "name", "lore", "amount", "datavalue", "glow", "nbt", "slot");

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PluginDisableListener(), this);
        if (pm.isPluginEnabled("hClaims")) {
            pm.registerEvents(new HClaimsProtectListeners(), this);
        }
        getCommand("home").setExecutor(new HomeCommand());

        HomeSettings.serverVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];
        config = new Yaml(getDataFolder() + "/config.yml", "config.yml");
    }
}