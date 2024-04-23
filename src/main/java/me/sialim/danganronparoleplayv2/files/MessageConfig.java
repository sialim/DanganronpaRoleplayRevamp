package me.sialim.danganronparoleplayv2.files;

import me.sialim.danganronparoleplayv2.DanganronpaRoleplayV2;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessageConfig {
    private File file;
    private FileConfiguration customFile;

    public void setup () {
        DanganronpaRoleplayV2 plugin = DanganronpaRoleplayV2.getPlugin(DanganronpaRoleplayV2.class);
        file = new File(plugin.getDataFolder (), "messages.yml");

        if (!file.exists ()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }

        customFile = YamlConfiguration.loadConfiguration (file);
    }

    public FileConfiguration get () {
        return customFile;
    }

    public void save () {
        try {
            customFile.save(file);
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public void reload () {
        customFile = YamlConfiguration.loadConfiguration (file);
    }

    public void addDefaults () {
        if(!get().contains ("voting")) {
            // Messages
            get().addDefault ("voting.start-message", "&aVoting started.");
            get().addDefault("voting.end-message","&cVoting ended.");
            get().addDefault("voting.results-announcement", "&aVoting results:");
            get().addDefault("voting.results-message", "&6$player$ : $votes$ votes");
            get().addDefault("voting.inventory-name", "&6&lPlayer Voting");
            get().addDefault("voting.inventory-player-name","&6$player$");
            get().addDefault("voting.player-vote-announcement", "&6You voted for $player$");
            get().addDefault("voting.result-state", "&aWill results show when everyone is done voting? $boolean$");



            get().addDefault("blacklist.list-player-name","&6$player$");
            get().addDefault("blacklist.player-removed","&a$player$ successfully removed from blacklist.");
            get().addDefault("blacklist.player-added","&a$player$ successfully added to blacklist.");

            get().addDefault("timer.state","&aTimer set to $boolean$");
            get().addDefault("timer.over", "&cTimer finished.");
            get().addDefault("timer.bossbar-text", "&c&lTime Remaining: $time$s");

            // Error messages
            get().addDefault("blacklist.errors.player-not-in-blacklist","&c$player$ not found in blacklist.");
            get().addDefault("blacklist.errors.player-already-in-blacklist","&cBlacklist already contains $player$");
            get().addDefault("errors.players-only", "Only players can execute this command.");
            get().addDefault ("voting.errors.voting-in-progress", "&cVoting is already in progress.");
            get().addDefault ("voting.errors.voting-not-in-progress", "&cVoting isn't in progress.");
            get().addDefault("voting.errors.blacklisted-restriction","&cSorry, blacklisted players cannot vote!");
            get().addDefault("voting.errors.player-not-found", "&cPlayer not found or offline.");
            get().addDefault("timer.error.args", "&cOne argument expected.");
            get().addDefault("timer.error.invalid-int", "&cPlease enter an integer.");
        }
    }
}
