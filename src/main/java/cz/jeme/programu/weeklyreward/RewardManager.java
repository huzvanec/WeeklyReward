package cz.jeme.programu.weeklyreward;

import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RewardManager {
    public List<Reward> rewards = new ArrayList<>();
    public List<ItemStack> items = new ArrayList<>();
    private final Random random = new Random();

    public RewardManager() {
        List<Map<?, ?>> rewardsList = Config.config.getMapList("rewards");

        for (Map<?, ?> map : rewardsList) {
            rewards.add(new Reward(map));
        }
        rewards = Collections.unmodifiableList(rewards);

        for (Reward reward : rewards) {
            for (int i = 0; i < reward.chance; i++) {
                items.add(new ItemStack(reward.item));
            }
        }
        items = Collections.unmodifiableList(items);
    }

    public ItemStack getRandom() {
        return items.get(random.nextInt(items.size()));
    }
}