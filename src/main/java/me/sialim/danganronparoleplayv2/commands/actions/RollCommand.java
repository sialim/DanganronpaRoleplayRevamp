package me.sialim.danganronparoleplayv2.commands.actions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class RollCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(sender.hasPermission("danganrp.roll"))
        {
            if(sender instanceof Player)
            {
                Player p = (Player) sender;
                if(args.length==0){
                    int rollResult = new Random().nextInt(100) + 1;
                    p.getServer().broadcastMessage(p.getDisplayName() + ChatColor.GRAY + " выпало число " + ChatColor.WHITE + rollResult + ChatColor.GRAY + " [1-100]");
                }
                else
                {
                    try
                    {
                        int rollMax = Integer.parseInt(args[0]);
                        if(rollMax<=1)
                        {
                            p.sendMessage(ChatColor.RED + "[DRP] Number must be greater than zero.");
                        }

                        int rollResult = new Random().nextInt(rollMax) + 1;
                        p.getServer().broadcastMessage( p.getDisplayName() + ChatColor.GRAY + " выпало число " + ChatColor.WHITE + rollResult + ChatColor.GRAY + " [1-" + rollMax + "]");
                    }
                    catch (NumberFormatException e){p.sendMessage(ChatColor.RED + "[DRP] Invalid integer.");}
                }
            }
        }else{sender.sendMessage(ChatColor.RED + "[DRP] Sorry, you do not have permission to perform this command.");}
        return true;
    }
}

