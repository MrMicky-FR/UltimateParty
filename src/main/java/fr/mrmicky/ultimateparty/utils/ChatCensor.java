package fr.mrmicky.ultimateparty.utils;

import com.google.common.base.Strings;
import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.regex.Pattern;

public class ChatCensor {

    private static final Pattern URL_PATTERN = Pattern.compile("(?i)[a-zA-Z0-9\\-.]+\\s?(\\.|dot|\\(dot\\)|\\(\\.\\)|-|;|:|\\(\\)|,)\\s?(com|org|net|cz|co|uk|sk|biz|mobi|xxx|eu|me|gg)\\b");

    private final UltimateParty plugin;
    private final ProxiedPlayer player;

    private String message;
    private boolean cancel = false;

    public ChatCensor(UltimateParty plugin, ProxiedPlayer player, String message) {
        this.plugin = plugin;
        this.player = player;
        this.message = message;

        checkUrl();
        checkSwear();
    }

    public boolean isCancel() {
        return cancel;
    }

    public String getNewMessage() {
        return message;
    }

    private void checkUrl() {
        if (plugin.getConfig().getBoolean("Chat.PreventUrl") && URL_PATTERN.matcher(message).find()) {
            cancel = true;
            Message.NO_URL_CHAT.send(player);
        }
    }

    private void checkSwear() {
        for (String word : message.split(" ")) {
            for (String s : plugin.getConfig().getStringList("Chat.BlockedWords")) {
                if (!s.isEmpty() && StringUtils.containsIgnoreCase(word, s)) {
                    message = censor(s);
                }
            }
        }
    }

    private String censor(String word) {
        String replace = Strings.repeat("*", word.length());

        return Pattern.compile(word, Pattern.CASE_INSENSITIVE).matcher(message).replaceAll(replace);
    }
}
