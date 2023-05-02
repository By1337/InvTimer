package org.by1337.invtimer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.by1337.bairdrop.api.event.AirDropOpenEvent;

public class AirDropListener implements Listener {
    @EventHandler
    public void open(AirDropOpenEvent e){
        if(InvTimer.instance.getConfig().getStringList("airdrops").contains(e.getAirDrop().getAirId())){
            e.setCancelled(true);
            Inv inv = new Inv(e.getAirDrop(), e.getPlayer());
            e.getPlayer().openInventory(inv.getInventory());
        }
    }
}
