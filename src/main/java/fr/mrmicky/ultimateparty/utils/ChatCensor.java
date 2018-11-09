package fr.mrmicky.ultimateparty.utils;

import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ChatCensor {

    private static final Pattern PATTERN_URL = Pattern.compile(
            "(?i)[a-zA-Z0-9\\-.]+\\s?(\\.|dot|\\(dot\\)|\\(\\.\\)|-|;|:|\\(\\)|,)\\s?(com|org|net|cz|co|uk|sk|biz|mobi|xxx|eu|me|gg)\\b");

    private String msg;
    private ProxiedPlayer p;
    private boolean cancel = false;

    public ChatCensor(ProxiedPlayer p, String msg) {
        this.p = p;
        this.msg = msg;

        checkDomainsIp();
        checkSwear();
    }

    public boolean isCancel() {
        return cancel;
    }

    public String getNewMessage() {
        return msg;
    }

    private void checkDomainsIp() {
        if (UltimateParty.getInstance().getConfig().getBoolean("PreventUrl")) {
            if (PATTERN_URL.matcher(msg).find()) {
                cancel = true;
                p.sendMessage(Message.NO_URL_CHAT.getAsComponenent());
            }
        }
    }

    private void checkSwear() {
        for (String words : msg.split(" ")) {
            for (String s : UltimateParty.getInstance().getConfig().getStringList("Chat.WorldsBlacklist")) {
                if (!s.isEmpty() && words.toLowerCase().contains(s.toLowerCase())) {
                    msg = censor(s);
                }
            }
        }
    }

    private String censor(String word) {
        char[] chars = new char[word.length()];
        Arrays.fill(chars, '*');

        return Pattern.compile(word, Pattern.CASE_INSENSITIVE).matcher(msg).replaceAll(new String(chars));
    }
}