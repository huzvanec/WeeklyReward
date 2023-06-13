package cz.jeme.programu.weeklyreward;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class WeeklyRewardCommand extends Command {
    private final Config config;

    protected WeeklyRewardCommand(Config config) {
        super("weeklyreward", "The main weekly reward command", "", Collections.singletonList("wr"));
        this.config = config;
        register();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length < 1) {
            return notEnoughArguments(sender);
        }
        if (args[0].equals("reload")) {
            config.reload();
            sender.sendMessage(Messages.prefix("<green>Config reloaded successfully!</green>"));
            return true;
        }
        if (args.length < 3) {
            return notEnoughArguments(sender);
        }
        if (args[0].equals("setweek")) {
            String player = args[1];
            String uuid;
            Map<String, String> savedPlayers = getSavedPlayers();
            if (savedPlayers.containsValue(player)) {
                uuid = player;
            } else if (savedPlayers.containsKey(player)) {
                uuid = savedPlayers.get(player);
            } else {
                sender.sendMessage(Messages.prefix("<red>That player doesn't exist!</red>"));
                return true;
            }
            int week;
            try {
                week = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                return notValidWeek(sender);
            }
            if (week < 1 || week > 53) {
                return notValidWeek(sender);
            }
            Config.rewardLog.set(uuid + ".last-reward.week", week);
            Config.saveRewardLog();
            sender.sendMessage(Messages.prefix("<green>Week number changed successfully!"));
            return true;
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
       if (args.length == 1) {
           return List.of("reload", "setweek");
       }
       if (args[0].equals("setweek") && args.length == 2) {
           return new ArrayList<>(getSavedPlayers().keySet());
       }
       return new ArrayList<>();
    }

    private static boolean notEnoughArguments(CommandSender sender) {
        sender.sendMessage(Messages.prefix("<red>Not enough arguments!</red>"));
        return true;
    }

    private static boolean notValidWeek(CommandSender sender) {
        sender.sendMessage(Messages.prefix("<red>That is not a valid week number!</red>"));
        sender.sendMessage(Messages.prefix("<red>Week number is a number between 1 and 53!</red>"));
        return true;
    }

    private static Map<String, String> getSavedPlayers() {
        Map<String, String> players = new HashMap<>();
        for (String uuid : Config.rewardLog.getKeys(false)) {
            players.put(Config.rewardLog.getString(uuid + ".name"), uuid);
        }
        return players;
    }

    private void register() {
        Bukkit.getCommandMap().register("weeklyreward", this);
    }
}
