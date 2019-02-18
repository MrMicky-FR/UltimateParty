package fr.mrmicky.ultimateparty.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.List;

public class ChatUtils {

    // Just a useful method
    public static boolean containsIgnoreCase(List<String> list, String s) {
        for (String str : list) {
            if (str.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
