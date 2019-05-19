package fr.mrmicky.ultimateparty;

import com.google.common.io.ByteStreams;
import fr.mrmicky.ultimateparty.command.CommandParty;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.connection.PartyConnector;
import fr.mrmicky.ultimateparty.displayname.PartyNameProvider;
import fr.mrmicky.ultimateparty.listeners.PartyListener;
import fr.mrmicky.ultimateparty.locale.LocaleLoader;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.options.DataManager;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import fr.mrmicky.ultimateparty.utils.Checker;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

public final class UltimateParty extends Plugin {

    //public static final String USER_ID = "";
    //public static final String NONCE_ID = "";
    public static final String USER_ID = "%%__USER__%%";
    public static final String NONCE_ID = "%%__NONCE__%%";

    private static UltimateParty instance;

    private Configuration config;
    private CommandParty commandParty;
    private PartyManager partyManager;
    private DataManager dataManager;

    private PartyNameProvider displayNameProvider;
    private PartyConnector connector;

    public static UltimateParty getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Checker checker = new Checker(this);
        if (!checker.isValid()) {
            return;
        }

        instance = this;

        loadConfig();

        String command = config.getString("Commands.Command");
        String[] aliases = config.getStringList("Commands.Aliases").toArray(new String[0]);
        commandParty = new CommandParty(command, config.getBoolean("Commands.Permission"), aliases, this);

        getProxy().getPluginManager().registerCommand(this, commandParty);
        getProxy().getPluginManager().registerListener(this, new PartyListener(this));

        partyManager = new PartyManager(this);
        dataManager = new DataManager(this);

        PartyNameProvider.loadProvider(this);
        PartyConnector.loadConnector(this);

        if (config.getBoolean("CheckUpdates")) {
            getProxy().getScheduler().runAsync(this, checker::checkUpdate);
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
                try (InputStream in = getResourceAsStream("config.yml");
                     OutputStream out = new FileOutputStream(configFile)) {
                    ByteStreams.copy(in, out);
                }
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load configuration file", e);
        }

        new LocaleLoader(this);
    }

    public Configuration getConfig() {
        return config;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public String getDisplayName(ProxiedPlayer p) {
        if (displayNameProvider != null) {
            try {
                String displayName = displayNameProvider.getDisplayName(p);
                return displayName != null ? ChatUtils.color(displayName) : p.getName();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "An error occurred while getting display name for " + p.getName(), e);
            }
        }
        return p.getName();
    }

    public void setDisplayNameProvider(PartyNameProvider displayNameProvider) {
        this.displayNameProvider = displayNameProvider;
        if (displayNameProvider != null) {
            getLogger().info("Using " + displayNameProvider.getName() + " as DisplayNameProvider");
        }
    }

    public void connect(ProxiedPlayer p, ServerInfo server) {
        if (p.getServer().getInfo() == server) {
            Message.ALREADY_CONNECT.send(p);
            return;
        }

        if (connector != null) {
            try {
                connector.connect(p, server);
                return;
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "An error occurred while connecting " + p.getName(), e);
            }
        }

        p.connect(server);
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

    public void openMenu(ProxiedPlayer p) {
        commandParty.openMenu(p);
    }

    public boolean isServerEnable(ProxiedPlayer p) {
        return !ChatUtils.containsIgnoreCase(config.getStringList("DisableServers"), p.getServer().getInfo().getName());
    }

    public String getCommand() {
        return commandParty.getName();
    }
}
