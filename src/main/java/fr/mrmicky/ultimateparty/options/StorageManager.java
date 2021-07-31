package fr.mrmicky.ultimateparty.options;

import fr.mrmicky.ultimateparty.UltimateParty;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class StorageManager {

    private final UltimateParty plugin;
    private final File saveFile;
    private final Configuration saveConfiguration;

    public StorageManager(UltimateParty plugin) {
        this.plugin = plugin;

        saveFile = new File(plugin.getDataFolder(), "data.yml");

        try {
            if (!saveFile.exists()) {
                saveConfiguration = new Configuration();
            } else {
                saveConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(saveFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't load data file", e);
        }
    }

    public void setOption(ProxiedPlayer player, PartyOption option, boolean enable) {
        saveConfiguration.set(player.getUniqueId() + "." + option.getKey(), enable);
    }

    public boolean isOptionEnabled(ProxiedPlayer player, PartyOption option) {
        return this.isOptionEnabled(player.getUniqueId(), option);
    }

    public boolean isOptionEnabled(UUID player, PartyOption option) {
        return saveConfiguration.getBoolean(player + "." + option.getKey(), option.getDefaultValue());
    }

    public void saveData() {
        plugin.getProxy().getScheduler().runAsync(plugin, this::saveDataSync);
    }

    public void saveDataSync() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(saveConfiguration, saveFile);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save players file", e);
        }
    }
}
