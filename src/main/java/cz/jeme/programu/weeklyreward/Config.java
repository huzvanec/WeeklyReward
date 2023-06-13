package cz.jeme.programu.weeklyreward;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Config {
    protected static FileConfiguration config;
    protected static FileConfiguration rewardLog;

    private final File dataFolder;
    private static File rewardLogFile;
    private static final String REWARD_LOG_FILE_NAME = "reward-log.yml";
    public static RewardManager rewardManager;
    private final JavaPlugin plugin;

    public Config(File dataFolder, JavaPlugin plugin) {
        this.dataFolder = dataFolder;
        this.plugin = plugin;
    }

    public void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        rewardManager = new RewardManager();

        rewardLogFile = new File(dataFolder + File.separator + REWARD_LOG_FILE_NAME);
        try {
            rewardLogFile.createNewFile();
        } catch (IOException e) {
            WeeklyReward.serverLog(Level.SEVERE, "Couldn't create file " + rewardLogFile.getAbsolutePath() + "!");
            e.printStackTrace();
            return;
        }
        rewardLog = YamlConfiguration.loadConfiguration(rewardLogFile);
    }

    public static void saveRewardLog() {
        try {
            rewardLog.save(rewardLogFile);
        } catch (IOException e) {
            WeeklyReward.serverLog(Level.SEVERE, "Couldn't save file " + rewardLogFile.getAbsolutePath() + "!");
            e.printStackTrace();
        }
    }

}
