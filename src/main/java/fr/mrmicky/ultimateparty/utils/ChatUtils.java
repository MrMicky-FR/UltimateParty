package fr.mrmicky.ultimateparty.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public final class ChatUtils {

    private static final boolean HAS_HEX_COLORS;

    static {
        boolean hasHexColors = true;

        try {
            ChatColor.class.getMethod("of", String.class);
        } catch (NoSuchMethodException e) {
            hasHexColors = false;
        }

        HAS_HEX_COLORS = hasHexColors;
    }

    public static final TextComponent SPACE_COMPONENT = new TextComponent(" ");

    private ChatUtils() {
        throw new UnsupportedOperationException();
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String colorHex(String text) {
        String result = color(text);

        if (!HAS_HEX_COLORS) {
            return result;
        }

        int index = -1;
        while ((index = result.indexOf('<', index)) >= 0) {
            index++;

            if (index > result.length() - 8 || result.charAt(index) != '#') {
                continue;
            }

            if (result.charAt(index + 7) != '>') {
                continue;
            }

            ChatColor color = ChatColor.of(result.substring(index, index + 7));
            result = result.substring(0, index - 1) + color + result.substring(index + 8);
            index = -1;
        }

        return result;
    }

    public static BaseComponent[] colorToComponents(String text) {
        return TextComponent.fromLegacyText(color(text));
    }

    public static TextComponent newComponent(String text, ChatColor color) {
        TextComponent component = new TextComponent(text);
        component.setColor(color);
        return component;
    }
}
