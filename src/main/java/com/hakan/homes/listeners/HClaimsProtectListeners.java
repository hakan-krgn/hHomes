package com.hakan.homes.listeners;

import com.hakan.claimsystem.Claim;
import com.hakan.claimsystem.api.ClaimAPI;
import com.hakan.claimsystem.api.customevents.ClaimFriendRemoveEvent;
import com.hakan.homes.Home;
import com.hakan.homes.api.HomeAPI;
import com.hakan.homes.api.customevent.HomeCreateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class HClaimsProtectListeners implements Listener {

    @EventHandler
    public void onHomeCreate(HomeCreateEvent event) {
        Claim claim = ClaimAPI.getClaim(event.getHome().getLocation());
        if (claim != null) {
            Player player = event.getPlayer();
            if (!claim.getOwner().equals(player.getName()) && claim.getFriend(player.getName()) == null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onRemoveFriendFromClaim(ClaimFriendRemoveEvent event) {
        String playerName = event.getClaimFriend().getFriendName();

        Map<String, Home> homes = HomeAPI.getInstance().getPlayerHomes(playerName);
        for (Home home : homes.values()) {
            if (ClaimAPI.getClaim(home.getLocation()).equals(event.getClaim())) {
                HomeAPI.getInstance().deleteHome(playerName, home);
            }
        }
    }
}