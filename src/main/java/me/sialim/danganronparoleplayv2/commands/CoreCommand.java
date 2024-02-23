package me.sialim.danganronparoleplayv2.commands;

import me.sialim.danganronparoleplayv2.DanganronpaRoleplayV2;
import me.sialim.danganronparoleplayv2.files.MessageConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CoreCommand implements CommandExecutor {

    DanganronpaRoleplayV2 main;
    MessageConfig messageConfig;

    public CoreCommand(DanganronpaRoleplayV2 main, MessageConfig messageConfig) {
        this.main = main;
        this.messageConfig = messageConfig;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length >= 1) {
            switch (strings[0]) {
                case "reload" -> {
                    main.reloadConfig();

                    messageConfig.save();
                    messageConfig.reload();
                }
                // add more cases later
            }
        }
        return true;
    }
}
