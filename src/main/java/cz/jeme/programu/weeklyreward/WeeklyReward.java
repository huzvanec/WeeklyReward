package cz.jeme.programu.weeklyreward;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class WeeklyReward extends JavaPlugin {

    private final List<String> rewardList = new ArrayList<>();

    @Override
    public void onEnable() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new JoinEvent(getDataFolder().getAbsolutePath(), rewardList), this);
        File f = getDataFolder();
        if (!f.exists()) {
            f.mkdir();
        }
        rewardFileConfig();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("weeklyreward")) {
            if (sender instanceof Player) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw" + sender.getName()
                        + " [\"\",{\"text\":\"WeeklyReward\",\"color\":\"green\"},{\"text\":\" succesfully loaded :D!\",\"color\":\"gold\"}]");
            } else {
                sender.sendMessage("WeeklyReward successfully loaded :D!");
            }

            return true;
        }
        return false;
    }

    private void rewardFileConfig() {
        File rewardsFile = new File(getDataFolder(), "rewards-config.yml");
        // create rewards-config.yml
        if (!(rewardsFile.exists())) {
            try {
                rewardsFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().log(Level.SEVERE,
                        "WeeklyReward: Cannot create file \"rewards-config.yml\"", e);
            }
        }
        FileConfiguration rewardsConfigYml = YamlConfiguration.loadConfiguration(rewardsFile);
        ConfigurationSection section = rewardsConfigYml.getConfigurationSection("Rewards");
        if (section == null) {
            section = rewardsConfigYml.createSection("Rewards");

        }
        Set<String> weekRewards = section.getKeys(false);
        // file is empty, fill with default values
        if (weekRewards.size() == 0) {
            section.set("diamond 1", 15);
            section.set("emerald 20", 20);
            section.set("gold_ingot 15", 25);
            section.set("iron_ingot 25", 30);
            section.set("coal_block 5", 15);
            // save to rewards-config.yml
            try {
                rewardsConfigYml.save(rewardsFile);
            } catch (IOException e) {
                Bukkit.getServer().getLogger().log(Level.SEVERE,
                        "WeeklyReward: Cannot save file \"rewards-config.yml\"", e);
            }
            weekRewards = section.getKeys(false);
        }
        for (String reward : weekRewards) {
            int rewardCount = section.getInt(reward);
            for (int i = 0; i < rewardCount; i++) {
                rewardList.add(reward);
            }
        }
    }

}