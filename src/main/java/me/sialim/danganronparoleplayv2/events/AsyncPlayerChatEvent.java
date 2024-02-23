package me.sialim.danganronparoleplayv2.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncPlayerChatEvent implements Listener {
    @EventHandler
    public void onPlayerChat(org.bukkit.event.player.AsyncPlayerChatEvent event)
    {
        String originalMessage = event.getMessage();
        Player p = event.getPlayer();
        if(originalMessage.startsWith("+ ") || originalMessage.startsWith("\\"))
        {
            String newMessage = originalMessage.substring(2);
            event.setCancelled(true);
            p.getServer().broadcastMessage(ChatColor.GRAY + "[OOC] " + p.getDisplayName() + ChatColor.GRAY + ": " + newMessage);
        } else if (originalMessage.startsWith("\\ "))
        {
            String newMessage = originalMessage.substring(3);
            event.setCancelled(true);
            p.getServer().broadcastMessage(ChatColor.GRAY + "[OOC] " + p.getDisplayName() + ChatColor.GRAY + ": " + newMessage);
        } else if (originalMessage.startsWith("+"))
        {
            String newMessage = originalMessage.substring(1);
            event.setCancelled(true);
            p.getServer().broadcastMessage(ChatColor.GRAY + "[OOC] " + p.getDisplayName() + ChatColor.GRAY + ": " + newMessage);
        }
    }
}
