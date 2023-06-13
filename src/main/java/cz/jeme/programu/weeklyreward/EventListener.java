package cz.jeme.programu.weeklyreward;

import net.kyori.adventure.title.Title;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;

public class EventListener implements Listener {
    private final Calendar calendar = Calendar.getInstance();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        calendar.setTime(new Date());
        if (!shouldGetReward(player)) return;

        ItemStack item = giveReward(player);
        if (Config.config.getBoolean("logging.give")) {
            WeeklyReward.serverLog(Level.INFO,
                    player.getName() + " got " + item.getAmount() + "× [" + item.getType().toString().toLowerCase() + "]");
        }
        updateRewardLog(player, item);
        message(player, item);
    }

    private boolean shouldGetReward(Player player) {
        int lastWeek = Config.rewardLog.getInt(player.getUniqueId() + ".last-reward.week");

        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        return currentWeek != lastWeek;
    }

    private ItemStack giveReward(Player player) {
        Inventory inventory = player.getInventory();
        ItemStack item = Config.rewardManager.getRandom();

        Map<Integer, ItemStack> notStored = inventory.addItem(new ItemStack(item));

        final World world = player.getWorld();
        final Location location = player.getLocation();

        // Drop items that didn't fit in the player's inventory on the ground
        for (ItemStack notStoredItem : notStored.values()) {
            world.dropItem(location, notStoredItem);
        }
        return item;
    }

    private void updateRewardLog(Player player, ItemStack reward) {
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        ConfigurationSection playerSection = Config.rewardLog.createSection(player.getUniqueId().toString());
        playerSection.set("name", player.getName());
        playerSection.set("last-reward.login", DATE_FORMAT.format(new Date()));
        playerSection.set("last-reward.week", currentWeek);
        playerSection.set("last-reward.reward", reward.getAmount() + "× " + reward.getType().toString().toLowerCase());
        Config.saveRewardLog();
    }

    private void message(Player player, ItemStack item) {
        String title = Config.config.getString("messages.title.title");
        String subtitle = Config.config.getString("messages.title.subtitle");
        if (title == null) title = "";
        if (subtitle == null) subtitle = "";

        long fadeIn = Config.config.getLong("messages.title.fade-in");
        long stay = Config.config.getLong("messages.title.stay");
        long fadeOut = Config.config.getLong("messages.title.fade-out");

        final Title.Times times = Title.Times.times(
                Duration.ofMillis(fadeIn),
                Duration.ofMillis(stay),
                Duration.ofMillis(fadeOut)
        );

        player.showTitle(Title.title(
                Messages.from(formatMessage(title, item)),
                Messages.from(formatMessage(subtitle, item)),
                times
        ));

        String actionBar = Config.config.getString("messages.action-bar");
        if (actionBar != null) {
            player.sendActionBar(Messages.from(formatMessage(actionBar, item)));
        }

        String chat = Config.config.getString("messages.chat");
        if (chat != null) {
            player.sendMessage(Messages.from(formatMessage(chat, item)));
        }
    }

    @SuppressWarnings("deprecation")
    private static String formatMessage(String message, ItemStack item) {
        String name = WordUtils.capitalizeFully(item.getType().toString().replace('_', ' '));
        return message.replace("{ITEM}", name).replace("{AMOUNT}", String.valueOf(item.getAmount()));
    }
}