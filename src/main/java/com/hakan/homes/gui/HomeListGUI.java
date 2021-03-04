package com.hakan.homes.gui;

import com.hakan.homes.Home;
import com.hakan.homes.HomePlugin;
import com.hakan.homes.api.HomeAPI;
import com.hakan.homes.utils.HomeSettings;
import com.hakan.homes.utils.PlaySound;
import com.hakan.homes.utils.Utils;
import com.hakan.homes.utils.yaml.Yaml;
import com.hakan.icreator.utils.fyaml.YamlItem;
import com.hakan.invapi.InventoryAPI;
import com.hakan.invapi.inventory.invs.HInventory;
import com.hakan.invapi.inventory.invs.Pagination;
import com.hakan.invapi.inventory.item.ClickableItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeListGUI {

    public static void open(Player player) {
        Yaml config = HomePlugin.config;

        HInventory hInventory = InventoryAPI.getInventoryManager().setTitle(config.getString("gui-homelist.title")).setSize(config.getInt("gui-homelist.size")).setInventoryType(InventoryType.CHEST).setCloseable(true).setId("homelistgui_" + player.getName()).setClickable(true).create();
        hInventory.guiFill(new ItemStack(Material.AIR));

        Map<String, Home> playerHomes = HomeAPI.getInstance().getPlayerHomes(player.getName());

        Pagination pagination = hInventory.getPagination();
        pagination.setItemSlots(config.getIntegerList("gui-homelist.items.home-item.slots"));
        List<ClickableItem> clickableItems = new ArrayList<>();
        if (playerHomes != null) {
            YamlItem yamlItem = new YamlItem(HomePlugin.getInstance(), config.getFileConfiguration(), "gui-homelist.items.home-item");
            for (Map.Entry<String, Home> entry : playerHomes.entrySet()) {

                YamlItem homeItem = yamlItem.clone();

                String homeName = entry.getKey();
                Home home = entry.getValue();

                homeItem.setName(homeItem.getName().replace("%name%", homeName));
                homeItem.setLore(Utils.replaceList(homeItem.getLore(), "%x%", home.getLocation().getBlockX() + ""));
                homeItem.setLore(Utils.replaceList(homeItem.getLore(), "%y%", home.getLocation().getBlockY() + ""));
                homeItem.setLore(Utils.replaceList(homeItem.getLore(), "%z%", home.getLocation().getBlockZ() + ""));
                clickableItems.add(ClickableItem.of(homeItem.complete(), inventoryClickEvent -> {
                    PlaySound.playButtonClick(player);
                    if (inventoryClickEvent.getClick().equals(ClickType.LEFT)) {
                        HomeSettings.useHomeCommand.put(player.getName(), true);
                        hInventory.close(player);
                        HomeAPI.getInstance().teleport(player, home, isTeleported -> {
                            if (isTeleported) {
                                player.teleport(home.getLocation());
                                PlaySound.playExperienceOrb(player);
                            } else {
                                PlaySound.playVillagerNo(player);
                            }
                            HomeSettings.useHomeCommand.remove(player.getName());
                        });
                    } else if (inventoryClickEvent.getClick().equals(ClickType.RIGHT)) {
                        HomeSettingsGUI.open(player, home);
                    }
                }));
            }
        }
        pagination.setItems(clickableItems);

        YamlItem nextPage = new YamlItem(HomePlugin.getInstance(), config.getFileConfiguration(), "gui-homelist.items.next-page");
        hInventory.setItem(nextPage.getSlot(), ClickableItem.of(nextPage.complete(), inventoryClickEvent -> {
            PlaySound.playButtonClick(player);
            pagination.nextPage();
        }));

        YamlItem previousPage = new YamlItem(HomePlugin.getInstance(), config.getFileConfiguration(), "gui-homelist.items.previous-page");
        hInventory.setItem(previousPage.getSlot(), ClickableItem.of(previousPage.complete(), inventoryClickEvent -> {
            PlaySound.playButtonClick(player);
            pagination.previousPage();
        }));

        YamlItem back = new YamlItem(HomePlugin.getInstance(), config.getFileConfiguration(), "gui-homelist.items.back");
        hInventory.setItem(back.getSlot(), ClickableItem.of(back.complete(), inventoryClickEvent -> {
            PlaySound.playButtonClick(player);
            MainGUI.open(player);
        }));

        hInventory.open(player);
    }
}