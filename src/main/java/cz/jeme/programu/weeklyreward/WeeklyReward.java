package cz.jeme.programu.weeklyreward;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class WeeklyReward extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new EventListener(), this);

        saveDefaultConfig();

        Config config = new Config(getDataFolder(), this);
        config.reload();

        new WeeklyRewardCommand(config);
    }


    public static void serverLog(Level level, String message) {
        Bukkit.getLogger().log(level, Messages.strip(Messages.PREFIX) + message);
    }

}