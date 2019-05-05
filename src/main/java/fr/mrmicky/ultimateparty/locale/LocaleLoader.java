package fr.mrmicky.ultimateparty.locale;

import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class LocaleLoader {

    private final UltimateParty plugin;

    private Configuration lang;
    private File file;

    public LocaleLoader(UltimateParty plugin) {
        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), "messages.yml");

        if (file.exists()) {
            loadMessages();
        } else {
            plugin.getLogger().info("messages.yml not founded - Creating it...");
            createFile();
        }
    }

    private void createFile() {
        try {
            file.createNewFile();
            lang = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            for (Message msg : Message.values()) {
                lang.set(msg.toString().toLowerCase().replace('_', '-'), msg.getMessage().replace(ChatColor.COLOR_CHAR, '&'));
            }

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(lang, file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to create messages file", e);
        }
    }

    private void loadMessages() {
        try {
            lang = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            boolean save = false;

            for (Message msg : Message.values()) {
                String replace = msg.toString().toLowerCase().replace('_', '-');
                String message = lang.getString(replace);
                if (message != null && !message.isEmpty()) {
                    msg.setMessage(ChatUtils.color(message));
                } else {
                    // message is missing, so we add it
                    lang.set(replace, msg.getMessage().replace(ChatColor.COLOR_CHAR, '&'));
                    save = true;
                }
            }

            // We save only if we added new messages
            if (save) {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(lang, file);
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to load messages file", e);
        }
    }
}
