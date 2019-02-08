package fr.mrmicky.ultimateparty.options;

import fr.mrmicky.ultimateparty.UltimateParty;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DataManager {

    private UltimateParty m;

    private File file;
    private Configuration save;

    public DataManager(UltimateParty m) {
        this.m = m;

        file = new File(m.getDataFolder(), "data.yml");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            save = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            throw new RuntimeException("Can't load data file", e);
        }
    }

    public void setOption(ProxiedPlayer p, PartyOption option, boolean enable) {
        save.set(p.getUniqueId().toString() + '.' + option.toString().toLowerCase(), enable);
    }

    public boolean getOption(ProxiedPlayer p, PartyOption option) {
        return save.getBoolean(p.getUniqueId().toString() + '.' + option.toString().toLowerCase(), option.getDefaultValue());
    }

    public void saveData() {
        m.getProxy().getScheduler().runAsync(m, this::saveDataSync);
    }

    public void saveDataSync() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(save, file);
        } catch (IOException e) {
            m.getLogger().log(Level.SEVERE, "Could not save the messages", e);
        }
    }
}
