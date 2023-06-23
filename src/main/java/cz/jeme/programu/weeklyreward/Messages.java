package cz.jeme.programu.weeklyreward;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class Messages {
    public static final String PREFIX = "<dark_gray>[</dark_gray><gold>WeeklyReward</gold><dark_gray>]: </dark_gray>";
    public static final MiniMessage MESSAGE = MiniMessage.miniMessage();

    private Messages() {
    }

    public static Component from(String string) {
        return MESSAGE.deserialize(string);
    }

    public static Component prefix(String string) {
        return MESSAGE.deserialize(PREFIX + string);
    }

    public static String strip(String string) {
        return MESSAGE.stripTags(string);
    }
}
