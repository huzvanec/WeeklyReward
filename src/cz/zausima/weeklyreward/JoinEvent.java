package cz.zausima.weeklyreward;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

	private String dataFolderName;
	private List<String> rewardList;
	

	public JoinEvent(String dataFolderName, List<String> rewardList) {
		this.dataFolderName = dataFolderName;
		this.rewardList = rewardList;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Bukkit.getServer().getLogger().log(Level.INFO, "WeeklyReward: Event onPlayerLogin detected activity");
		Player p = event.getPlayer();
		File weekFile = new File(dataFolderName, "weekslog.yml");
		// create weekslog.yml
		if (!(weekFile.exists())) {
			try {
				weekFile.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getLogger().log(Level.SEVERE, "WeeklyReward: Cannot create file \"weekslog.yml\"",
						e);
			}
		}
		FileConfiguration weekLog = YamlConfiguration.loadConfiguration(weekFile);
		ConfigurationSection section = weekLog.getConfigurationSection("Weeks");
		if (section == null) {
			section = weekLog.createSection("Weeks");
		}
		Set<String> playerNames = section.getKeys(false);
		String playerName = p.getName();
		// player is not in weekslog.yml, add him
		if (!playerNames.contains(playerName)) {
			section.set(playerName, 0);
		}
		// player is (already) in weekslog.yml
		Integer playerWeekInt = (Integer) section.get(playerName);
		int playerWeek = 0;
		if (playerWeekInt != null) {
			playerWeek = playerWeekInt.intValue();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		int weekInYear = cal.get(Calendar.WEEK_OF_YEAR);
		if (playerWeek != weekInYear) {
			// gets reward
			section.set(playerName, weekInYear);
			Bukkit.getServer().getLogger().log(Level.INFO,
					"WeeklyReward: Player " + playerName + " should get reward!");
			generateReward(playerName);

		}

		// save weekslog.yml
		try {
			weekLog.save(weekFile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "WeeklyReward: Cannot save file \"weekslog.yml\"", e);
		}
	}

	private void generateReward(String playerName) {
		Random r = new Random();
		int rewardIndex = r.nextInt(rewardList.size());
		String reward = rewardList.get(rewardIndex);
		String[] rewardParts = reward.split(" ");
		String rewardCount = rewardParts[rewardParts.length - 1];
		String rewardName = rewardParts[0].replace("_", " ");
		if (!rewardCount.equals("1")) {
			rewardName = rewardName + "s";
		}
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + playerName
				+ " [\"\",{\"text\":\"===========================\",\"color\":\"gold\"},{\"text\":\"\\n"
				+ " \\u0020 \\u0020 \\u0020 \\u0020\"},{\"text\":\"Weekly Reward!\",\"color\":\"green\"}"
				+ ",{\"text\":\" \\u0020 \\u0020 \\u0020 \\n\\n \\u0020 \\u0020 \\u0020\"},{\"text\":\"You " + "got "
				+ rewardCount + " " + rewardName
				+ "!\",\"color\":\"aqua\"},{\"text\":\"\\n\"},{\"text\":\"===========================\",\"color\""
				+ ":\"gold\"},{\"text\":\"\\n \"}]");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + playerName + " " + reward);
		Bukkit.getServer().getLogger().log(Level.INFO, "WeeklyReward: " + playerName + " got " + rewardCount + " " + rewardName);
	}
}