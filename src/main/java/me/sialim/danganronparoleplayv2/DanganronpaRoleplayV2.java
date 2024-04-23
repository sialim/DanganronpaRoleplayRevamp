package me.sialim.danganronparoleplayv2;

import me.sialim.danganronparoleplayv2.commands.CoreCommand;
import me.sialim.danganronparoleplayv2.commands.actions.*;
import me.sialim.danganronparoleplayv2.events.AsyncPlayerChatEvent;
import me.sialim.danganronparoleplayv2.files.MessageConfig;
import me.sialim.danganronparoleplayv2.timer.Timer;
import me.sialim.danganronparoleplayv2.voting.VotingManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DanganronpaRoleplayV2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Default config.yml setup
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Default messages.yml setup
        MessageConfig messageConfig = new MessageConfig();
        messageConfig.setup();
        messageConfig.get().options().copyDefaults(true);
        messageConfig.addDefaults();
        messageConfig.save();

        // Class declaration (to make registration easier later)
        // ^ Commands
        CoreCommand coreCommand = new CoreCommand(this, messageConfig);
        MeCommand meCommand = new MeCommand();
        RollCommand rollCommand = new RollCommand();
        NonRpCommand nonRpCommand = new NonRpCommand();
        DoCommand doCommand = new DoCommand();
        TryCommand tryCommand = new TryCommand();
        Timer timer = new Timer(this, messageConfig);

        // ^ Listeners
        AsyncPlayerChatEvent asyncPlayerChatEvent = new AsyncPlayerChatEvent();

        // ^ Other
        VotingManager votingManager = new VotingManager(this, messageConfig);

        // Command registration
        getCommand("me").setExecutor(meCommand);

        getCommand("roll").setExecutor(rollCommand);

        getCommand("b").setExecutor(nonRpCommand);

        getCommand("do").setExecutor(doCommand);

        getCommand("try").setExecutor(tryCommand);

        getCommand("voting").setExecutor(votingManager);
        getCommand("voting").setTabCompleter(votingManager);

        getCommand("dg").setExecutor(coreCommand);
        getCommand("dg").setTabCompleter(coreCommand);

        getCommand("timer").setExecutor(timer);

        // Event registration
        getServer().getPluginManager().registerEvents(asyncPlayerChatEvent, this);
        getServer().getPluginManager().registerEvents(votingManager, this);
    }
}
