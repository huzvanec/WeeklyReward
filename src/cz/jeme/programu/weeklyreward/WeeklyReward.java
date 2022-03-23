package cz.jeme.programu.weeklyreward;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WeeklyReward extends JavaPlugin {
	
	private List<String> rewardList = new ArrayList<String>();
	
	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new JoinEvent(getDataFolder().getAbsolutePath(), rewardList), this);
		File f = getDataFolder();
		if(!f.exists()) {
		    f.mkdir();
		}
		rewardFileConfig();
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("weeklyreward")) {
			if (sender instanceof Player) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw" + sender.getName() +
						" [\"\",{\"text\":\"WeeklyReward\",\"color\":\"green\"},{\"text\":\" succesfully loaded :D!\",\"color\":\"gold\"}]");
			} else {
				sender.sendMessage("WeeklyReward successfully loaded :D!");	
			}
			
			return true;
		}
		return false;
	}

	public void registerEvents() {
	}

	
	private void rewardFileConfig() {
		File rewardsFile = new File(getDataFolder(), "rewards.yml");
		// create rewards.yml
		if (!(rewardsFile.exists())) {
			try {
				rewardsFile.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getLogger().log(Level.SEVERE, "WeeklyReward: Cannot create file \"rewards.yml\"", e);
			}
		}
		FileConfiguration rewardsYml = YamlConfiguration.loadConfiguration(rewardsFile);
		ConfigurationSection section = rewardsYml.getConfigurationSection("Rewards");
		if (section == null) {
			section = rewardsYml.createSection("Rewards");

		}
		Set<String> weekRewards = section.getKeys(false);
		// file is empty, fill with default values
		if (weekRewards.size() == 0) {
			section.set("diamond 1", 15);
			section.set("emerald 20", 20);
			section.set("gold_ingot 15", 25);
			section.set("iron_ingot 25", 30);
			section.set("coal_block 5", 15);
			// save to rewards.yml
			try {
				rewardsYml.save(rewardsFile);
			} catch (IOException e) {
				Bukkit.getServer().getLogger().log(Level.SEVERE, "WeeklyReward: Cannot save file \"rewards.yml\"", e);
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