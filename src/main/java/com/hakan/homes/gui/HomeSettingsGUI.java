package com.hakan.homes.gui;

import com.hakan.homes.Home;
import com.hakan.homes.HomePlugin;
import com.hakan.homes.PlayerData;
import com.hakan.homes.api.HomeAPI;
import com.hakan.homes.api.customevent.HomeDeleteEvent;
import com.hakan.homes.utils.PlaySound;
import com.hakan.homes.utils.Utils;
import com.hakan.icreator.utils.fyaml.YamlItem;
import com.hakan.invapi.InventoryAPI;
import com.hakan.invapi.inventory.invs.HInventory;
import com.hakan.invapi.inventory.item.ClickableItem;
import com.hakan.messageplugin.api.MessageAPI;
import com.hakan.signapi.HSign;
import com.hakan.signapi.SignAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HomeSettingsGUI {

    public static void open(Player player, Home home) {
        FileConfiguration config = HomePlugin.config.getFileConfiguration();

        HInventory hInventory = InventoryAPI.getInventoryManager().setTitle(config.getString("gui-home-settings.title").replace("%name%", home.getHomeName())).setSize(config.getInt("gui-home-settings.size")).setInventoryType(InventoryType.CHEST).setCloseable(true).setId("homesettingsgui_" + player.getName()).setClickable(true).create();
        hInventory.guiFill(new ItemStack(Material.AIR));

        YamlItem renameItem = new YamlItem(HomePlugin.getInstance(), config, "gui-home-settings.items.rename");
        hInventory.setItem(renameItem.getSlot(), ClickableItem.of(renameItem.complete(), (event) -> {
            PlaySound.playButtonClick(player);

            HSign hSign = SignAPI.getSignManager().setType(Material.valueOf("SIGN_POST")).setLines(config.getStringList("settings.rename-home-lines")).create();
            hSign.open(player, new HSign.SignCallback() {
                @Override
                public void whenOpened(String[] strings) {

                }

                @Override
                public void whenClosed(String[] strings) {
                    String homeName = strings[0];

                    if (homeName.length() < 3) {
                        PlaySound.playVillagerNo(player);
                        open(player, home);
                        return;
                    }

                    PlayerData playerData = HomeAPI.getInstance().getPlayerData(player.getName());
                    if (playerData != null) {
                        if (playerData.hasHome(homeName)) {
                            PlaySound.playVillagerNo(player);
                            player.sendMessage(Utils.getText(config, "messages.there-is-home-this-name"));
                            return;
                        }
                    }

                    PlaySound.playExperienceOrb(player);

                    HomeAPI.getInstance().deleteHome(player.getName(), home);
                    home.setHomeName(homeName);
                    playerData.setHome(homeName, home);

                    String title = Utils.getText(config, "settings.home-rename-title").replace("%name%", homeName);
                    String subtitle = Utils.getText(config, "settings.home-rename-subtitle").replace("%name%", homeName);
                    MessageAPI.getTitleAPI().getTitleManager().setTitle(title).setSubtitle(subtitle).setFadeIn(5).setStay(80).setFadeOut(5).create().send(player);
                }
            });
        }));

        YamlItem transportItem = new YamlItem(HomePlugin.getInstance(), config, "gui-home-settings.items.transport");
        hInventory.setItem(transportItem.getSlot(), ClickableItem.of(transportItem.complete(), (event) -> {

            List<String> world = config.getStringList("settings.disabled-worlds");
            if (world.contains(player.getWorld().getName())) {
                player.sendMessage(Utils.getText(config, "messages.disabled-world"));
                return;
            }

            PlaySound.playButtonClick(player);
            hInventory.close(player);

            Location location = player.getLocation();
            home.setLocation(location);

            String title = Utils.getText(config, "settings.home-transport-title").replace("%x%", location.getBlockX() + "").replace("%y%", location.getBlockY() + "").replace("%z%", location.getBlockZ() + "");
            String subtitle = Utils.getText(config, "settings.home-transport-subtitle").replace("%x%", location.getBlockX() + "").replace("%y%", location.getBlockY() + "").replace("%z%", location.getBlockZ() + "");
            MessageAPI.getTitleAPI().getTitleManager().setTitle(title).setSubtitle(subtitle).setStay(80).setFadeOut(5).setFadeIn(5).create().send(player);

        }));

        YamlItem deleteItem = new YamlItem(HomePlugin.getInstance(), config, "gui-home-settings.items.delete");
        hInventory.setItem(deleteItem.getSlot(), ClickableItem.of(deleteItem.complete(), (event) -> {
            HomeDeleteEvent homeDeleteEvent = new HomeDeleteEvent(player, home);
            Bukkit.getPluginManager().callEvent(homeDeleteEvent);
            if (homeDeleteEvent.isCancelled()) {
                return;
            }

            PlaySound.playButtonClick(player);
            hInventory.close(player);

            HomeAPI.getInstance().deleteHome(player.getName(), home);

            String title = Utils.getText(config, "settings.home-delete-title").replace("%name%", home.getHomeName());
            String subtitle = Utils.getText(config, "settings.home-delete-subtitle").replace("%name%", home.getHomeName());
            MessageAPI.getTitleAPI().getTitleManager().setTitle(title).setSubtitle(subtitle).setStay(80).setFadeOut(5).setFadeIn(5).create().send(player);
        }));

        YamlItem backItem = new YamlItem(HomePlugin.getInstance(), config, "gui-home-settings.items.back");
        hInventory.setItem(backItem.getSlot(), ClickableItem.of(backItem.complete(), (event) -> {
            PlaySound.playButtonClick(player);
            HomeListGUI.open(player);
        }));

        hInventory.open(player);
    }
}