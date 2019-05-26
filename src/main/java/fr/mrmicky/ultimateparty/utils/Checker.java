package fr.mrmicky.ultimateparty.utils;

import fr.mrmicky.ultimateparty.UltimateParty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Checker {

    private final UltimateParty plugin;

    public Checker(UltimateParty plugin) {
        this.plugin = plugin;
    }

    public void checkUpdate() {
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
        } catch (IOException e) {
            // ignore
        }
    }

    public boolean isValid() {
        String rawUrl = String.format("https://mrmicky.fr/verify.php?plugin=%s&uid=%s&nonce=%s&version=%s", plugin.getDescription().getName(), UltimateParty.USER_ID, UltimateParty.NONCE_ID, plugin.getDescription().getVersion());

        try {
            StringBuilder str = new StringBuilder();

            URL url = new URL(rawUrl);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String inputLine;

                while ((inputLine = br.readLine()) != null) {
                    str.append(inputLine).append('%');
                }
            }

            String result = str.toString();

            if (result.contains(UltimateParty.USER_ID) || result.contains("refused")) {
                plugin.getLogger().severe(" ");
                plugin.getLogger().severe("*** THIS PLUGIN ID IS BLACKLISTED ! Please contact MrMicky on SpigotMC ! ***");
                if (result.contains("id=") && result.contains("%")) {
                    plugin.getLogger().severe("*** " + result.split("%")[1] + " ***");
                }
                plugin.getLogger().severe(" ");

                return false;
            }
        } catch (Exception e) {
            // ignore
        }
        return true;
    }
}
