package fr.mrmicky.ultimateparty.locale;

import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import fr.mrmicky.ultimateparty.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;

public class MessagesManager {

    private final Map<Message, String> messages = new EnumMap<>(Message.class);

    private final UltimateParty plugin;
    private final File messagesFile;

    public MessagesManager(UltimateParty plugin) {
        this.plugin = plugin;

        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
    }

    public void loadMessages() {
        try {
            Configuration messagesConfiguration;

            if (messagesFile.exists()) {
                messagesConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(messagesFile);
            } else {
                messagesConfiguration = new Configuration();
            }

            boolean hasNewMessages = false;

            for (Message message : Message.values()) {
                String path = StringUtils.formatEnum(message.toString());
                String rawMessage = messagesConfiguration.getString(path);

                if (rawMessage != null && !rawMessage.isEmpty()) {
                    messages.put(message, ChatUtils.colorHex(rawMessage));
                } else {
                    hasNewMessages = true;

                    messagesConfiguration.set(path, message.getDefaultMessage().replace(ChatColor.COLOR_CHAR, '&'));
                }
            }

            if (hasNewMessages) {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(messagesConfiguration, messagesFile);

                plugin.getLogger().warning("Missing messages was added to messages.yml");
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to load messages", e);
        }
    }

    String getMessage(Message message) {
        String rawMessage = messages.get(message);

        return rawMessage != null ? rawMessage : message.getDefaultMessage();
    }
}
