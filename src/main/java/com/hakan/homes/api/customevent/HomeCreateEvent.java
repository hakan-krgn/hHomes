package com.hakan.homes.api.customevent;

import com.hakan.homes.Home;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HomeCreateEvent extends Event implements Cancellable {

    private final Player player;
    private final Home home;
    private boolean cancel;

    public HomeCreateEvent(Player player, Home home) {
        this.player = player;
        this.home = home;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Home getHome() {
        return home;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}