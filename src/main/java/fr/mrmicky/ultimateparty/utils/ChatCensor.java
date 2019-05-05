package fr.mrmicky.ultimateparty.utils;

import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ChatCensor {

    private static final Pattern URL_PATTERN = Pattern.compile("(?i)[a-zA-Z0-9\\-.]+\\s?(\\.|dot|\\(dot\\)|\\(\\.\\)|-|;|:|\\(\\)|,)\\s?(com|org|net|cz|co|uk|sk|biz|mobi|xxx|eu|me|gg)\\b");

    private final ProxiedPlayer player;

    private String message;
    private boolean cancel = false;

    public ChatCensor(ProxiedPlayer player, String message) {
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
        if (UltimateParty.getInstance().getConfig().getBoolean("Chat.PreventUrl")) {
            if (URL_PATTERN.matcher(message).find()) {
                cancel = true;
                Message.NO_URL_CHAT.send(player);
            }
        }
    }

    private void checkSwear() {
        for (String words : message.split(" ")) {
            for (String s : UltimateParty.getInstance().getConfig().getStringList("Chat.WorldsBlacklist")) {
                if (!s.isEmpty() && words.toLowerCase().contains(s.toLowerCase())) {
                    message = censor(s);
                }
            }
        }
    }

    private String censor(String word) {
        char[] chars = new char[word.length()];
        Arrays.fill(chars, '*');

        return Pattern.compile(word, Pattern.CASE_INSENSITIVE).matcher(message).replaceAll(new String(chars));
    }
}
