package fr.mrmicky.ultimateparty.locale;

import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LocaleLoader {

    private Configuration lang;
    private File file;

    public LocaleLoader(UltimateParty m) {
        file = new File(m.getDataFolder(), "messages.yml");

        if (file.exists()) {
            loadMessages();
        } else {
            m.getLogger().info("messages.yml not founded - Creating it...");
            createFile();
        }
    }

    public void createFile() {
        try {
            file.createNewFile();
            lang = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            for (Message msg : Message.values()) {
                lang.set(msg.toString().toLowerCase().replace('_', '-'), msg.message.replace(ChatColor.COLOR_CHAR, '&'));
            }

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(lang, file);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create messages file", e);
        }
    }

    private void loadMessages() {
        try {
            lang = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            boolean save = false;

            for (Message msg : Message.values()) {
                String message = lang.getString(msg.toString().toLowerCase().replace('_', '-'));
                if (message != null && !message.isEmpty()) {
                    msg.setMessage(ChatUtils.color(message));
                } else {
                    // message is missing, so we add it
                    lang.set(msg.toString().toLowerCase().replace('_', '-'), msg.message.replace(ChatColor.COLOR_CHAR, '&'));
                    save = true;
                }
            }

            // We save only if we added new messages
            if (save) {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(lang, file);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load messages file", e);
        }
    }
}
