package com.hakan.homes.gui;

import com.hakan.homes.Home;
import com.hakan.homes.HomePlugin;
import com.hakan.homes.PlayerData;
import com.hakan.homes.api.HomeAPI;
import com.hakan.homes.utils.PlaySound;
import com.hakan.homes.utils.Utils;
import com.hakan.icreator.utils.fyaml.YamlItem;
import com.hakan.invapi.api.InventoryAPI;
import com.hakan.invapi.inventory.invs.HInventory;
import com.hakan.invapi.inventory.item.ClickableItem;
import com.hakan.messageplugin.api.MessageAPI;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class HomeSettingsGUI {

    public static void open(Player player, Home home) {
        FileConfiguration config = HomePlugin.config.getFileConfiguration();

        HInventory hInventory = InventoryAPI.getInventoryManager().setTitle(config.getString("gui-home-settings.title").replace("%name%", home.getHomeName())).setSize(config.getInt("gui-home-settings.size")).setInventoryType(InventoryType.CHEST).setCloseable(true).setId("homesettingsgui_" + player.getName()).setClickable(true).create();
        hInventory.guiFill(new ItemStack(Material.AIR));

        YamlItem renameItem = new YamlItem(HomePlugin.getInstance(), config, "gui-home-settings.items.rename");
        hInventory.setItem(renameItem.getSlot(), ClickableItem.of(renameItem.complete(), (event) -> {
            PlaySound.playButtonClick(player);

            int playerLevel = player.getLevel();
            float playerExp = player.getExp();
            new AnvilGUI.Builder().onClose(player1 -> {
                new BukkitRunnable() {
                    public void run() {
                        player1.setLevel(playerLevel);
                        player1.setExp(playerExp);
                    }
                }.runTaskLater(HomePlugin.getInstance(), 2);
            }).onComplete((player1, text) -> {

                PlayerData playerData = HomeAPI.getInstance().getPlayerData(player.getName());

                if (playerData != null) {
                    if (playerData.hasHome(text)) {
                        PlaySound.playVillagerNo(player);
                        player.sendMessage(Utils.getText(config, "messages.there-is-home-this-name"));
                        return AnvilGUI.Response.close();
                    }
                }

                PlaySound.playExperienceOrb(player);

                HomeAPI.getInstance().deleteHome(player.getName(), home);
                home.setHomeName(text);
                playerData.setHome(text, home);

                String title = Utils.getText(config, "settings.home-rename-title").replace("%name%", text);
                String subtitle = Utils.getText(config, "settings.home-rename-subtitle").replace("%name%", text);
                MessageAPI.getTitleAPI().getTitleManager().setTitle(title).setSubtitle(subtitle).setFadeIn(5).setStay(80).setFadeOut(5).create().send(player);

                return AnvilGUI.Response.close();

            }).text(home.getHomeName()).itemLeft(new ItemStack(Material.PAPER)).title("§aHome Rename").plugin(HomePlugin.getInstance()).open(player);

        }));

        YamlItem transportItem = new YamlItem(HomePlugin.getInstance(), config, "gui-home-settings.items.transport");
        hInventory.setItem(transportItem.getSlot(), ClickableItem.of(transportItem.complete(), (event) -> {

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