package fr.mrmicky.ultimateparty;

import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.command.PartyMainCommand;
import fr.mrmicky.ultimateparty.connection.PartyConnector;
import fr.mrmicky.ultimateparty.displayname.PartyNameProvider;
import fr.mrmicky.ultimateparty.listeners.PartyListener;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.locale.MessagesManager;
import fr.mrmicky.ultimateparty.options.StorageManager;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import fr.mrmicky.ultimateparty.utils.StringUtils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.logging.Level;

public final class UltimateParty extends Plugin {

    private static UltimateParty instance;

    private Configuration config;
    private PartyMainCommand commandParty;
    private PartyManager partyManager;
    private MessagesManager messagesManager;
    private StorageManager dataManager;

    private PartyNameProvider displayNameProvider;
    private PartyConnector connector;

    public static UltimateParty getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        messagesManager = new MessagesManager(this);

        loadConfig();

        String command = config.getString("Commands.Command");
        String[] aliases = config.getStringList("Commands.Aliases").toArray(new String[0]);
        commandParty = new PartyMainCommand(command, config.getBoolean("Commands.Permission"), aliases, this);

        getProxy().getPluginManager().registerCommand(this, commandParty);
        getProxy().getPluginManager().registerListener(this, new PartyListener(this));

        partyManager = new PartyManager(this);
        dataManager = new StorageManager(this);

        PartyNameProvider.loadProvider(this);
        PartyConnector.loadConnector(this);

        if (config.getBoolean("CheckUpdates")) {
            getProxy().getScheduler().runAsync(this, this::checkUpdate);
        }
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveDataSync();
        }
    }

    public void loadConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                try (InputStream in = getResourceAsStream("config.yml")) {
                    Files.copy(in, configFile.toPath());
                }
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load configuration", e);
        }

        messagesManager.loadMessages();
    }

    public Configuration getConfig() {
        return config;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public StorageManager getDataManager() {
        return dataManager;
    }

    public String getDisplayName(ProxiedPlayer player) {
        if (displayNameProvider != null) {
            try {
                String displayName = displayNameProvider.getDisplayName(player);
                return displayName != null ? ChatUtils.color(displayName) : player.getName();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "An error occurred while getting display name for " + player.getName(), e);
            }
        }
        return player.getName();
    }

    public void setDisplayNameProvider(PartyNameProvider displayNameProvider) {
        this.displayNameProvider = displayNameProvider;
        if (displayNameProvider != null) {
            getLogger().info("Using " + displayNameProvider.getName() + " as DisplayNameProvider");
        }
    }

    public void connect(ProxiedPlayer player, ServerInfo server) {
        if (player.getServer().getInfo() == server) {
            Message.ALREADY_CONNECT.send(player);
            return;
        }

        if (connector != null) {
            try {
                connector.connect(player, server);
                return;
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "An error occurred while connecting " + player.getName(), e);
            }
        }

        player.connect(server);
    }

    public void setConnector(PartyConnector connector) {
        this.connector = connector;
        if (connector != null) {
            getLogger().info("Using " + connector + " as ServerConnector");
        }
    }

    public void registerCommand(PartyCommand cmd) {
        commandParty.register(cmd);
    }

    public void openMenu(ProxiedPlayer player) {
        commandParty.openMenu(player);
    }

    public boolean isServerEnable(ProxiedPlayer player) {
        return !StringUtils.containsIgnoreCase(config.getStringList("DisableServers"), player.getServer().getInfo().getName());
    }

    public String getCommand() {
        return commandParty.getName();
    }

    private void checkUpdate() {
        try {
            URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=51548");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String version = getDescription().getVersion();
                String lastVersion = reader.readLine();
                if (!version.equalsIgnoreCase(lastVersion)) {
                    getLogger().warning("A new version is available ! Last version is " + lastVersion + " and you are on " + version);
                    getLogger().warning("You can download it on: https://www.spigotmc.org/resources/ultimateparty.51548/");
                }
            }
        } catch (IOException e) {
            // ignore
        }
    }
}
