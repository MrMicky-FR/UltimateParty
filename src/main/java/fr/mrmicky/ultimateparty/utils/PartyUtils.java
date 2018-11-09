package fr.mrmicky.ultimateparty.utils;

import java.util.List;

public class PartyUtils {

    // Just a usefull method
    public static boolean containsIgnoreCase(List<String> list, String s) {
        for (String str : list) {
            if (str.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }
}
