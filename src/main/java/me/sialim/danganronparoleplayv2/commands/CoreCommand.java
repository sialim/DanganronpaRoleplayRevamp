package me.sialim.danganronparoleplayv2.commands;

import me.sialim.danganronparoleplayv2.DanganronpaRoleplayV2;
import me.sialim.danganronparoleplayv2.files.MessageConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CoreCommand implements TabExecutor {

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

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> options = new ArrayList<>();
        options.add("reload");
        if (strings.length >= 1) {
            return options;
        }
        return null;
    }
}
