package org.by1337.invtimer;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import org.by1337.bairdrop.util.Event;

import java.util.Objects;
import java.util.logging.Level;

public final class InvTimer extends JavaPlugin {
    public static InvTimer instance;

    @Override
    public void onLoad() {
        if(getServer().getPluginManager().getPlugin("BAirDrop") != null){
            try {
                Event.registerEvent(new Event(NamespacedKey.fromString("player_first_open_inv")));
                Event.registerEvent(new Event(NamespacedKey.fromString("player_open_inv")));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }else {
            this.getLogger().log(Level.SEVERE, "missing dependency: BAirDrop!");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getServer().getPluginManager().registerEvents(new AirDropListener(), this);
        this.saveDefaultConfig();
        Objects.requireNonNull(this.getCommand("itimer")).setExecutor(new Command());
        Objects.requireNonNull(this.getCommand("itimer")).setTabCompleter(new Command());
    }

    @Override
    public void onDisable() {
    }

}
