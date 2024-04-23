package me.sialim.danganronparoleplayv2.voting;

import me.sialim.danganronparoleplayv2.DanganronpaRoleplayV2;
import me.sialim.danganronparoleplayv2.files.MessageConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingManager implements TabExecutor, Listener {
    private final DanganronpaRoleplayV2 main;
    private final MessageConfig messageConfig;
    private boolean votingInProgress;
    private boolean useTimer;
    private boolean showResults;
    private Map<Player, Player> votes;
    private BukkitTask votingEndTask;

    private ArrayList<Player> blacklist = new ArrayList<Player>();

    public VotingManager (DanganronpaRoleplayV2 main, MessageConfig messageConfig) {
        this.main = main;
        this.votes = new HashMap<>();
        this.messageConfig = messageConfig;
    }

    public void startVoting () {
        this.votingInProgress = true;
        this.votes.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            openVotingGUI(player);
        }
        String votingStartedMessage = ChatColor.translateAlternateColorCodes('&',
                messageConfig.get().getString("voting.start-message"));
        main.getServer().broadcastMessage(votingStartedMessage);
        if (useTimer) votingEndTask = Bukkit.getScheduler().runTaskLater
                (main, this::endVoting, 20 * main.getConfig().getLong("voting-time-seconds"));
    }

    public void endVoting () {
        if (votingEndTask != null) {
            votingEndTask.cancel();
        }
        if (showResults) broadcastVotingResult();
        this.votingInProgress = false;
        String message = ChatColor.translateAlternateColorCodes('&',
                messageConfig.get().getString("voting.end-message"));
        main.getServer().broadcastMessage(message);
    }

    private void broadcastVotingResult () {
        Map<Player, Integer> voteCounts = new HashMap<>();

        for (Player votee : votes.values()) {
            voteCounts.put(votee, voteCounts.getOrDefault(votee, 0) + 1);
        }
        List<Map.Entry<Player, Integer>> sortedVotes = new ArrayList<>(voteCounts.entrySet());
        sortedVotes.sort(Map.Entry.comparingByValue());


        String votingResultsAnnouncement = ChatColor.translateAlternateColorCodes('&',
                messageConfig.get().getString("voting.results-announcement"));
        main.getServer().broadcastMessage(votingResultsAnnouncement);
        for (Map.Entry<Player, Integer> entry : sortedVotes) {
            String votingResultsMessage = ChatColor.translateAlternateColorCodes('&',
                    messageConfig.get().getString("voting.results-message"));
            votingResultsMessage = votingResultsMessage.replace("$player$", entry.getKey().getDisplayName());
            votingResultsMessage = votingResultsMessage.replace("$votes$", String.valueOf(entry.getValue()));
            main.getServer().broadcastMessage(votingResultsMessage);
        }
    }

    private ItemStack getPlayerSkull(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(player);
        skull.setItemMeta(meta);
        return skull;
    }

    public boolean isVotingInProgress () {
        return votingInProgress;
    }

    public void addVote (Player voter, Player votee) {
        votes.put(voter, votee);

        if (votes.size() >= (Bukkit.getOnlinePlayers().size() - blacklist.size())) {
            endVoting();
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player p) {
            if (p.isOp()) {
                if (args.length >= 1) {
                    switch (args[0]) {
                        case "start" -> {
                            if (isVotingInProgress()) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        messageConfig.get().getString("voting.errors.voting-in-progress")));
                            } else {
                                startVoting();
                            }
                        }
                        case "stop","end" -> {
                            if (!isVotingInProgress()) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        messageConfig.get().getString("voting.errors.voting-not-in-progress")));
                            } else {
                                endVoting();
                            }
                        } case "blacklist" -> {
                            if (args.length >= 3) {
                                Player target = Bukkit.getPlayer(args[2]);
                                switch (args[1]) {
                                    case "add" -> {
                                        if (blacklist.contains(target)) {
                                            String message = ChatColor.translateAlternateColorCodes('&',
                                                    messageConfig.get().getString("blacklist.errors.player-already-in-blacklist"));
                                            message = message.replace("$player$",target.getName());
                                            p.sendMessage(message);
                                        } else {
                                            String message = ChatColor.translateAlternateColorCodes('&',
                                                    messageConfig.get().getString("blacklist.player-added"));
                                            message = message.replace("$player$",target.getName());
                                            p.sendMessage(message);
                                            blacklist.add(target);
                                        }
                                    }
                                    case "remove" -> {
                                        if (blacklist.contains(target)) {
                                            String message = ChatColor.translateAlternateColorCodes('&',
                                                    messageConfig.get().getString("blacklist.player-removed"));
                                            message = message.replace("$player$",target.getName());
                                            p.sendMessage(message);
                                            blacklist.remove(target);
                                        } else {
                                            String message = ChatColor.translateAlternateColorCodes('&',
                                                    messageConfig.get().getString("blacklist.errors.player-not-in-blacklist"));
                                            message = message.replace("$player$",target.getName());
                                            p.sendMessage(message);
                                        }
                                    }
                                    default -> {
                                        for (Player player : blacklist) {
                                            String message = ChatColor.translateAlternateColorCodes('&',
                                                    messageConfig.get().getString("blacklist.list-player-name"));
                                            message = message.replace("$player$",player.getName());
                                            p.sendMessage(message);
                                        }
                                    }
                                }


                            } else {
                                for (Player player : blacklist) {
                                    String message = ChatColor.translateAlternateColorCodes('&',
                                            messageConfig.get().getString("blacklist.list-player-name"));
                                    message = message.replace("$player$",player.getName());
                                    p.sendMessage(message);
                                }
                            }
                        }
                        case "usetimer","timer" -> {
                            useTimer = !useTimer;
                            String message = ChatColor.translateAlternateColorCodes('&',
                                    messageConfig.get().getString("timer.state"));
                            message = message.replace("$boolean$",String.valueOf(useTimer));
                            p.sendMessage(message);
                        }
                        case "results","result" -> broadcastVotingResult();
                        case "showresults","showresult" -> {
                            showResults = !showResults;
                            String message = ChatColor.translateAlternateColorCodes('&',
                                    messageConfig.get().getString("voting.result-state"));
                            message = message.replace("$boolean$",String.valueOf(showResults));
                            p.sendMessage(message);
                        }
                    }

                }
            }
        } else {
            System.out.println(main.getConfig().getString("errors.players-only"));
        }
        return true;
    }

    public void openVotingGUI (Player p) {
        String title = ChatColor.translateAlternateColorCodes('&',
                messageConfig.get().getString("voting.inventory-name"));
        Inventory gui = Bukkit.createInventory(p, 27, title);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(!blacklist.contains(player)) {
                ItemStack playerHead = getPlayerSkull(player);
                ItemMeta itemMeta = playerHead.getItemMeta();

                String playerName = ChatColor.translateAlternateColorCodes('&',
                        messageConfig.get().getString("voting.inventory-player-name"));
                playerName = playerName.replace("$player$", player.getDisplayName());
                itemMeta.setDisplayName(playerName);
                playerHead.setItemMeta(itemMeta);
                gui.addItem(playerHead);
            }
        }
        p.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory clickedInventory = e.getClickedInventory();
        String title = ChatColor.translateAlternateColorCodes('&',
                messageConfig.get().getString("voting.inventory-name"));

        if (clickedInventory != null
                && clickedInventory.equals(p.getOpenInventory().getTopInventory())
                && e.getView().getTitle().equalsIgnoreCase(title)) {
            e.setCancelled(true);
            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                SkullMeta skullMeta = (SkullMeta) clickedItem.getItemMeta();
                if (skullMeta != null && skullMeta.hasOwner()) {
                    Player targetPlayer = Bukkit.getPlayer(skullMeta.getOwningPlayer().getName());
                    if (targetPlayer != null) {
                        if (blacklist.contains(p)) {
                            String message = ChatColor.translateAlternateColorCodes('&',
                                    messageConfig.get().getString("voting.errors.blacklisted-restriction"));
                            p.sendMessage(message);
                        } else {
                            addVote(p, targetPlayer);
                            String message = ChatColor.translateAlternateColorCodes('&',
                                    messageConfig.get().getString("voting.player-vote-announcement"));
                            message = message.replace("$player$",targetPlayer.getName());
                            p.sendMessage(message);
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                messageConfig.get().getString("voting.errors.player-not-found")));
                    }

                }



                p.closeInventory();
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> options1 = new ArrayList<>();
        options1.add("start");
        options1.add("stop");
        options1.add("blacklist");
        options1.add("timer");
        options1.add("results");
        options1.add("showresults");
        if (strings.length == 1) {
            return options1;
        }

        if (strings.length == 2) {
            if (strings[0].equals("blacklist")) {
                List<String> options2 = new ArrayList<>();
                options2.add("add");
                options2.add("remove");
                if (strings.length == 3) {
                    List<String> playerNames = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        playerNames.add(p.getDisplayName());
                    }
                    return playerNames;
                }
            }

        }

        return null;

    }
}
