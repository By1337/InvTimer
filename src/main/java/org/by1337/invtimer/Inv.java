package org.by1337.invtimer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import org.by1337.bairdrop.AirDrop;
import org.by1337.bairdrop.api.event.AirDropEndEvent;
import org.by1337.bairdrop.util.Event;
import org.by1337.bairdrop.util.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inv implements Listener {
    private final AirDrop airDrop;
    private final Player player;
    private final Inventory inventory;
    private int time = InvTimer.instance.getConfig().getInt("open-time");
    private int taskId;

    public Inv(AirDrop airDrop, Player player) {
        this.airDrop = airDrop;
        this.player = player;
        inventory = Bukkit.createInventory(null, 27, Message.messageBuilder(InvTimer.instance.getConfig().getString("inv-name")));
        Bukkit.getServer().getPluginManager().registerEvents(this, InvTimer.instance);
        generate();
    }
    private void generate(){
        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                if(time == 0){
                    if(!airDrop.isItWasOpen()){
                        airDrop.event(Event.getByKey(NamespacedKey.fromString("player_first_open_inv")), player);
                        airDrop.setItWasOpen(true);
                    }else {
                        airDrop.event(Event.getByKey(NamespacedKey.fromString("player_open_inv")), player);
                    }
                    player.openInventory(airDrop.getInv());
                }
                ItemStack itemStack = new ItemStack(Material.valueOf(InvTimer.instance.getConfig().getString("material")));
                ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.setDisplayName(Message.messageBuilder(Objects.requireNonNull(InvTimer.instance.getConfig().getString("item-name")).replace("{time}", String.valueOf(time))));

                List<String> lore = new ArrayList<>(InvTimer.instance.getConfig().getStringList("item-lore"));
                lore.replaceAll(Message::messageBuilder);
                itemMeta.setLore(lore);

                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                itemStack.setItemMeta(itemMeta);

                for(int x = 0; x < inventory.getSize(); x++){
                    inventory.setItem(x, itemStack);
                }
                time--;
            }
        }.runTaskTimer(InvTimer.instance, 0, 20).getTaskId();
    }
    public Inventory getInventory() {
        return inventory;
    }

    @EventHandler
    public void close(InventoryCloseEvent e){
        HandlerList.unregisterAll(this);
        Bukkit.getServer().getScheduler().cancelTask(taskId);
    }
    @EventHandler
    public void end(AirDropEndEvent e){
        if(e.getAirDrop().getAirId().equals(airDrop.getAirId())){
            HandlerList.unregisterAll(this);
            Bukkit.getServer().getScheduler().cancelTask(taskId);
            player.closeInventory();
        }
    }
    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getInventory().equals(inventory))
            e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntity().getUniqueId().equals(player.getUniqueId()))
            player.closeInventory();
    }
    @EventHandler
    public void move(PlayerMoveEvent e){
        if(e.getPlayer().equals(player)){
            double dist = airDrop.getAirLoc().distance(player.getLocation());
            if(dist > 10){
                player.closeInventory();
            }
        }
    }
}
