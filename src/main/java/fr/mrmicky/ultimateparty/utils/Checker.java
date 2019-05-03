package fr.mrmicky.ultimateparty.utils;

import fr.mrmicky.ultimateparty.UltimateParty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Checker {

    private final UltimateParty plugin;

    private boolean valid = true;
    private String username = "";

    public Checker(UltimateParty plugin) {
        this.plugin = plugin;
        checkPluginYml();
        checkValid();
        loadUsername();

        if (valid) {
            plugin.getProxy().getScheduler().runAsync(plugin, this::checkUpdate);
        }
    }

    private void checkValid() {
        try {
            StringBuilder str = new StringBuilder();

            URL url = new URL(String.format("https://mrmicky.fr/verify.php?plugin=%s&uid=%s&nonce=%s&version=%s", plugin.getDescription().getName(), UltimateParty.USER_ID, UltimateParty.NONCE_ID, plugin.getDescription().getVersion()));

            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String inputLine;

                while ((inputLine = br.readLine()) != null) {
                    str.append(inputLine).append('%');
                }
            }

            String result = str.toString();

            if (result.contains(UltimateParty.USER_ID) || result.contains("refused")) {
                valid = false;
                plugin.getLogger().severe(" ");
                plugin.getLogger().severe("*** THIS PLUGIN ID IS BLACKLISTED ! Please contact MrMicky on SpigotMC ! ***");
                if (result.contains("id=") && result.contains("%")) {
                    plugin.getLogger().severe("***  REASON: " + result.split("%")[1] + " ***");
                }
                plugin.getLogger().severe("*** THE PLUGIN WILL DISABLE NOW ***");
                plugin.getLogger().severe(" ");
            }
        } catch (Exception e) {
        }
    }

    private void checkPluginYml() {
        if (!plugin.getDescription().getName().equals("UltimateParty") || !plugin.getDescription().getAuthor().equals("MrMicky")) {
            valid = false;
            plugin.getLogger().severe(" ");
            plugin.getLogger().severe("THE PLUGIN.YML HAS BEEN EDITED (NAME OR AUTHOR) ! PLEASE DOWNLOAD THE PLUGIN FROM SPIGOTMC AGAIN !");
            plugin.getLogger().severe("***THE PLUGIN WILL DISABLE***");
            plugin.getLogger().severe(" ");
        }
    }

    private void loadUsername() {
        try {
            StringBuilder str = new StringBuilder();

            URL url = new URL("https://www.spigotmc.org/members/" + UltimateParty.USER_ID);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;

                while ((inputLine = br.readLine()) != null) {
                    str.append(inputLine);
                }
            }

            username = str.toString().split("<title>")[1].split("</title>")[0].split(" | ")[0] + " ";
        } catch (Exception e) {
        }
    }

    private void checkUpdate() {
        try {
            URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=51548");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String version = plugin.getDescription().getVersion();
                String lastVersion = reader.readLine();
                if (!version.equalsIgnoreCase(lastVersion)) {
                    plugin.getLogger().warning("A new version is available ! Last version is " + lastVersion + " and you are on " + version);
                    plugin.getLogger().warning("You can download it on: https://www.spigotmc.org/resources/ultimateparty.51548/");
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean isValid() {
        return valid;
    }

    public String getUsername() {
        return username;
    }
}
