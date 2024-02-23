package me.sialim.danganronparoleplayv2.commands.actions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NonRpCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(sender.hasPermission("danganrp.ooc"))
        {
            if(sender instanceof Player)
            {
                Player p = (Player) sender;
                if(args.length==0){p.sendMessage(ChatColor.RED + "[DRP] Используйте команду: /b <message>");}
                else
                {
                    String message = String.join(" ", args);
                    p.getServer().broadcastMessage(ChatColor.GRAY + "[OOC] " + p.getDisplayName() + ChatColor.GRAY + ": " + message);
                }
            }
        }else{sender.sendMessage(ChatColor.RED + "[DRP] Sorry, you do not have permission to perform this command.");}
        return true;
    }
}
