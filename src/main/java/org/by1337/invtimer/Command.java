package org.by1337.invtimer;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.by1337.bairdrop.util.Message;

import java.util.List;

public class Command implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(args.length > 0){
            if (args[0].equalsIgnoreCase("reload")){
                if(sender.hasPermission("timer.reload")){
                    InvTimer.instance.reloadConfig();
                    sender.sendMessage(Message.messageBuilder("&aSuccessful reload"));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if(sender.hasPermission("timer.reload")){
            return List.of("reload");
        }
        return null;
    }
}
