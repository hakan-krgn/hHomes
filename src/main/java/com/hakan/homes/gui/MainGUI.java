package com.hakan.homes.gui;

import com.hakan.homes.HomePlugin;
import com.hakan.homes.PlayerData;
import com.hakan.homes.api.HomeAPI;
import com.hakan.homes.utils.HomeSettings;
import com.hakan.homes.utils.PlaySound;
import com.hakan.homes.utils.Utils;
import com.hakan.icreator.utils.fyaml.YamlItem;
import com.hakan.invapi.api.InventoryAPI;
import com.hakan.invapi.inventory.invs.HInventory;
import com.hakan.invapi.inventory.item.ClickableItem;
import com.hakan.messageplugin.api.MessageAPI;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MainGUI {

    public static void open(Player player) {
        FileConfiguration config = HomePlugin.config.getFileConfiguration();

        HInventory hInventory = InventoryAPI.getInventoryManager().setTitle(config.getString("gui-main.title")).setSize(config.getInt("gui-main.size")).setInventoryType(InventoryType.CHEST).setCloseable(true).setId("homegui_" + player.getName()).setClickable(true).create();
        hInventory.guiFill(new ItemStack(Material.AIR));

        YamlItem homeListItem = new YamlItem(HomePlugin.getInstance(), config, "gui-main.items.home-list-item");
        hInventory.setItem(homeListItem.getSlot(), ClickableItem.of(homeListItem.complete(), (event) -> {
            PlaySound.playButtonClick(player);
            HomeListGUI.open(player);
        }));

        YamlItem homeCreateItem = new YamlItem(HomePlugin.getInstance(), config, "gui-main.items.home-create-item");
        hInventory.setItem(homeCreateItem.getSlot(), ClickableItem.of(homeCreateItem.complete(), (event) -> {
            PlaySound.playButtonClick(player);

            YamlItem houseClickItem = new YamlItem(HomePlugin.getInstance(), config, "gui-create-home.items.house-item");
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
                    int maxHouse = HomeSettings.getMaxHome(player);
                    if (playerData.hasHome(text)) {
                        PlaySound.playVillagerNo(player);
                        player.sendMessage(Utils.getText(config, "messages.there-is-home-this-name"));
                        return AnvilGUI.Response.close();
                    } else if (playerData.getHomeList().size() >= maxHouse) {
                        PlaySound.playVillagerNo(player);
                        player.sendMessage(Utils.getText(config, "messages.maximum-purchaseable-home").replace("%count%", maxHouse + ""));
                        return AnvilGUI.Response.close();
                    }
                }

                PlaySound.playExperienceOrb(player);
                Location location = player.getLocation();

                HomeAPI.getInstance().setHome(player.getName(), location, text);

                String title = Utils.getText(config, "settings.home-set-title").replace("%x%", location.getBlockX() + "").replace("%y%", location.getBlockY() + "").replace("%z%", location.getBlockZ() + "").replace("%name%", text);
                String subtitle = Utils.getText(config, "settings.home-set-subtitle").replace("%x%", location.getBlockX() + "").replace("%y%", location.getBlockY() + "").replace("%z%", location.getBlockZ() + "").replace("%name%", text);
                MessageAPI.getTitleAPI().getTitleManager().setTitle(title).setSubtitle(subtitle).setFadeIn(5).setStay(80).setFadeOut(5).create().send(player);

                return AnvilGUI.Response.close();

            }).text(ChatColor.translateAlternateColorCodes('&', houseClickItem.getName())).itemLeft(houseClickItem.complete()).title(ChatColor.translateAlternateColorCodes('&', config.getString("gui-create-home.title"))).plugin(HomePlugin.getInstance()).open(player);
        }));

        hInventory.open(player);
    }
}