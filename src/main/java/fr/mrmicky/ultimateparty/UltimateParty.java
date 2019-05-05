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
import java.util.List;
import java.util.logging.Level;

public final class UltimateParty extends Plugin {

    //public static final String USER_ID = "12345";
    //public static final String NONCE_ID = "Test123";
    public static final String USER_ID = "%%__USER__%%";
    public static final String NONCE_ID = "%%__NONCE__%%";

    private static UltimateParty instance;
    private PartyManager partyManager;
    private Configuration config;
    private String command;
    private CommandParty commandParty;
    private DataManager dataManager;

    private List<String> disableServers;
    private List<String> disableAutoJoin;

    private PartyNameProvider displayNameProvider;
    private PartyConnector connector;

    public static UltimateParty getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Checker c = new Checker(this);
        if (!c.isValid()) {
            return;
        }

        instance = this;
        reloadConfig();

        command = config.getString("Commands.Command");
        dataManager = new DataManager(this);

        String[] aliases = config.getStringList("Commands.Aliases").toArray(new String[0]);
        commandParty = new CommandParty(command, config.getBoolean("Commands.Permission"), aliases, this);

        getProxy().getPluginManager().registerCommand(this, commandParty);
        getProxy().getPluginManager().registerListener(this, new PartyListener(this));

        partyManager = new PartyManager(this);

        PartyNameProvider.loadProvider(this);
        PartyConnector.loadConnector(this);

        getLogger().info("Thank you " + c.getUsername() + " for purchasing UltimateParty :)");
        getLogger().info("The plugin has been successfully activated");
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveDataSync();
        }
    }

    public void reloadConfig() {
        loadConfig();
        new LocaleLoader(this);
        disableServers = config.getStringList("DisableServers");
        disableAutoJoin = config.getStringList("DisableAutoJoinServers");
    }

    private void loadConfig() {
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

    public String getCommand() {
        return command;
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
        return !ChatUtils.containsIgnoreCase(disableServers, p.getServer().getInfo().getName());
    }

    public List<String> getDisableAutoJoin() {
        return disableAutoJoin;
    }
}
