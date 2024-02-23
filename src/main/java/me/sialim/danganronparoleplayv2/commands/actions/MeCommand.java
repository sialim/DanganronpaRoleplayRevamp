package me.sialim.danganronparoleplayv2.commands.actions;

import me.sialim.danganronparoleplayv2.DanganronpaRoleplayV2;
import me.sialim.danganronparoleplayv2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeCommand implements CommandExecutor
{
    DanganronpaRoleplayV2 plugin = DanganronpaRoleplayV2.getPlugin(DanganronpaRoleplayV2.class);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(sender.hasPermission("danganrp.me"))
        {
            if(sender instanceof Player)
            {
                Player p = (Player) sender;
                if(args.length==0){p.sendMessage(ChatColor.RED + "[DRP] Используйте команду: /me <action>");}
                else
                {
                    String message = String.join(" ", args);
                    double pX=p.getLocation().getX();
                    double pY=p.getLocation().getY();
                    double pZ=p.getLocation().getZ();
                    for(Player online: Bukkit.getOnlinePlayers())
                    {
                        double oX=online.getLocation().getX();
                        double oY=online.getLocation().getY();
                        double oZ=online.getLocation().getZ();
                        if(Utils.isWithinRadius(pX,pY,pZ,oX,oY,oZ,plugin.getConfig().getInt("command-radii.me")))
                            online.sendMessage(p.getDisplayName() + " " + ChatColor.GRAY + message);
                    }
                }
            }
        }else{sender.sendMessage(ChatColor.RED + "[DRP] Sorry, you do not have permission to perform this command.");}
        return true;
    }
}
