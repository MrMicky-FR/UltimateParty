package fr.mrmicky.ultimateparty.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public final class ChatUtils {

    public static final TextComponent SPACE_COMPONENT = new TextComponent(" ");

    private ChatUtils() {
        throw new UnsupportedOperationException();
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
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
