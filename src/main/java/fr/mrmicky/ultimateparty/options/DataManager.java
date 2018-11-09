package fr.mrmicky.ultimateparty.options;

import fr.mrmicky.ultimateparty.UltimateParty;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DataManager {

    private UltimateParty m;

    private File file;
    private Configuration save;

    public DataManager(UltimateParty m, File file) {
        this.file = file;
        this.m = m;

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
        save.set(p.getUniqueId().toString() + "." + option.toString().toLowerCase(), enable);
    }

    public boolean getOption(ProxiedPlayer p, PartyOption option) {
        if (save.contains(p.getUniqueId().toString() + "." + option.toString().toLowerCase())) {
            return save.getBoolean(p.getUniqueId().toString() + "." + option.toString().toLowerCase());
        }

        return option.getDefaultValue();
    }

    public void saveDatas() {
        saveDatas(true);
    }

    public void saveDatas(boolean async) {
        Runnable runnable = () -> {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(save, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        if (async) {
            m.getProxy().getScheduler().runAsync(m, runnable);
        } else {
            runnable.run();
        }
    }
}
