package cz.jeme.programu.weeklyreward;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.logging.Level;

public class Reward {
    public int chance;
    public ItemStack item;

    public Reward(Map<?, ?> map) {
        Object oName = map.get("item");
        if (oName == null) {
            logMissing("item");
            throw new NullPointerException();
        }
        String name = (String) oName;
        Material material = Material.getMaterial(name.toUpperCase());
        if (material == null) {
            WeeklyReward.serverLog(Level.SEVERE, "Error while reading config! " +
                    "Are you sure that \"" + name + "\" is a valid item?");
            throw new NullPointerException();
        }
        item = new ItemStack(material);

        Object oChance = map.get("chance");
        if (oChance == null) {
            logMissing("chance");
            throw new NullPointerException();
        }
        chance = (int) oChance;

        Object oAmount = map.get("amount");
        if (oAmount == null) {
            logMissing("amount");
            throw new NullPointerException();
        }

        item.setAmount((int) oAmount);
    }

    private static void logMissing(String parameter) {
        WeeklyReward.serverLog(Level.SEVERE, "Error while reading config! " +
                "There is a reward without \"" + parameter + "\"!");
    }
}
