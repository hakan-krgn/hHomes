package com.hakan.homes;

import com.hakan.homes.cmd.HomeCommand;
import com.hakan.homes.listeners.PluginDisableListener;
import com.hakan.homes.utils.HomeSettings;
import com.hakan.homes.utils.Metrics;
import com.hakan.homes.utils.VaultHook;
import com.hakan.homes.utils.yaml.Yaml;
import com.hakan.icreator.ItemCreator;
import com.hakan.invapi.api.InventoryAPI;
import com.hakan.messageplugin.api.MessageAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
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

        Bukkit.getPluginManager().registerEvents(new PluginDisableListener(), this);
        getCommand("home").setExecutor(new HomeCommand());

        HomeSettings.serverVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];
        config = new Yaml(getDataFolder() + "/config.yml", "config.yml");

        new Metrics(this, 10266);
        new VaultHook().setupEconomy().setupPermissions();
        InventoryAPI.setupInvs(this);
        MessageAPI.setup(this);
        ItemCreator.setup(this, "type", "name", "lore", "amount", "datavalue", "glow", "nbt", "slot");
    }
}