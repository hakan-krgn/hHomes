package com.hakan.homes.gui;

import com.hakan.homes.Home;
import com.hakan.homes.HomePlugin;
import com.hakan.homes.PlayerData;
import com.hakan.homes.api.HomeAPI;
import com.hakan.homes.api.customevent.HomeCreateEvent;
import com.hakan.homes.utils.HomeSettings;
import com.hakan.homes.utils.PlaySound;
import com.hakan.homes.utils.Utils;
import com.hakan.homes.utils.VaultHook;
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

public class MainGUI {

    public static void open(Player player) {
        FileConfiguration config = HomePlugin.config.getFileConfiguration();

        HInventory hInventory = InventoryAPI.getInventoryManager().setTitle(config.getString("gui-main.title")).setSize(config.getInt("gui-main.size")).setInventoryType(InventoryType.CHEST).setCloseable(true).setId("homegui_" + player.getName()).setClickable(true).create();
        hInventory.guiFill(new ItemStack(Material.AIR));

        PlayerData playerData = HomeAPI.getInstance().getPlayerData(player.getName());

        YamlItem homeListItem = new YamlItem(HomePlugin.getInstance(), config, "gui-main.items.home-list-item");
        hInventory.setItem(homeListItem.getSlot(), ClickableItem.of(homeListItem.complete(), (event) -> {
            PlaySound.playButtonClick(player);
            HomeListGUI.open(player);
        }));

        YamlItem homeCreateItem = new YamlItem(HomePlugin.getInstance(), config, "gui-main.items.home-create-item");
        hInventory.setItem(homeCreateItem.getSlot(), ClickableItem.of(homeCreateItem.complete(), (event) -> {
            PlaySound.playButtonClick(player);

            HSign hSign = SignAPI.getSignManager().setType(Material.valueOf("SIGN_POST")).setLines(config.getStringList("settings.create-home-lines")).create();
            hSign.open(player, new HSign.SignCallback() {
                @Override
                public void whenOpened(String[] strings) {
                }

                @Override
                public void whenClosed(String[] strings) {
                    String homeName = strings[0];

                    Location location = player.getLocation();

                    Home home = new Home(player.getName(), location, homeName);
                    HomeCreateEvent homeCreateEvent = new HomeCreateEvent(player, home);
                    Bukkit.getPluginManager().callEvent(homeCreateEvent);
                    if (homeCreateEvent.isCancelled()) {
                        return;
                    }

                    if (homeName.length() < 3) {
                        PlaySound.playVillagerNo(player);
                        open(player);
                        return;
                    }

                    if (playerData != null) {
                        int maxHouse = HomeSettings.getMaxHome(player);
                        if (playerData.hasHome(homeName)) {
                            PlaySound.playVillagerNo(player);
                            player.sendMessage(Utils.getText(config, "messages.there-is-home-this-name"));
                            return;
                        } else if (playerData.getHomeList().size() >= maxHouse) {
                            PlaySound.playVillagerNo(player);
                            player.sendMessage(Utils.getText(config, "messages.maximum-purchaseable-home").replace("%count%", maxHouse + ""));
                            return;
                        } else if (VaultHook.getEconomy().getBalance(player) < HomeSettings.getSethomeMoney(player)) {
                            PlaySound.playVillagerNo(player);
                            player.sendMessage(Utils.getText(config, "messages.sethome-money"));
                            return;
                        }
                    }

                    PlaySound.playExperienceOrb(player);

                    HomeAPI.getInstance().setHome(player.getName(), home);

                    String title = Utils.getText(config, "settings.home-set-title").replace("%x%", location.getBlockX() + "").replace("%y%", location.getBlockY() + "").replace("%z%", location.getBlockZ() + "").replace("%name%", homeName);
                    String subtitle = Utils.getText(config, "settings.home-set-subtitle").replace("%x%", location.getBlockX() + "").replace("%y%", location.getBlockY() + "").replace("%z%", location.getBlockZ() + "").replace("%name%", homeName);
                    MessageAPI.getTitleAPI().getTitleManager().setTitle(title).setSubtitle(subtitle).setFadeIn(5).setStay(80).setFadeOut(5).create().send(player);
                }
            });
        }));

        hInventory.open(player);
    }
}