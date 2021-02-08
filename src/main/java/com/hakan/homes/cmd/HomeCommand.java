package com.hakan.homes.cmd;

import com.hakan.homes.HomePlugin;
import com.hakan.homes.gui.MainGUI;
import com.hakan.homes.utils.HomeSettings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equals("home")) {
            if (args.length == 0) {
                if (commandSender instanceof Player) {
                    Player player = ((Player) commandSender);
                    if (HomeSettings.useHomeCommand.get(player.getName()) != null) {
                        return true;
                    }
                    MainGUI.open(player);
                }
            } else if (args.length == 1) {
                if (args[0].equals("reload")) {
                    if (commandSender.isOp()) {
                        HomePlugin.config.reload();
                        commandSender.sendMessage("§aHome system reloaded");
                    }
                }
            }
        }
        return true;
    }
}