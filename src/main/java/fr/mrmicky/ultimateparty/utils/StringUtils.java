package fr.mrmicky.ultimateparty.utils;

import java.util.List;

public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean containsIgnoreCase(List<String> list, String str) {
        for (String s : list) {
            if (s.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsIgnoreCase(String str, String search) {
        int length = search.length();
        int max = str.length() - length;

        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, search, 0, length)) {
                return true;
            }
        }
        return false;
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return str.length() >= prefix.length() && str.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static String formatEnum(String text) {
        return text.toLowerCase().replace('_', '-');
    }
}
