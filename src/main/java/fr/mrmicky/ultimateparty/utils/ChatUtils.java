package fr.mrmicky.ultimateparty.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public final class ChatUtils {

    public static final TextComponent SPACE = new TextComponent(" ");

    private ChatUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean containsIgnoreCase(List<String> list, String s) {
        for (String str : list) {
            if (str.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean startsWithIgnoreCase(String s, String prefix) {
        return s.length() >= prefix.length() && s.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static BaseComponent[] colorComponents(String s) {
        return TextComponent.fromLegacyText(color(s));
    }

    public static TextComponent coloredComponent(String s, ChatColor color) {
        TextComponent component = new TextComponent(s);
        component.setColor(color);
        return component;
    }
}
