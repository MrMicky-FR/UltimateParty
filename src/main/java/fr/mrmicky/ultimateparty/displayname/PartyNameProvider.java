package fr.mrmicky.ultimateparty.displayname;

import com.google.common.base.Strings;
import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.displayname.providers.BungeePermsProvider;
import fr.mrmicky.ultimateparty.displayname.providers.LuckPermsProvider;
import fr.mrmicky.ultimateparty.displayname.providers.PowerfulPermsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;

public interface PartyNameProvider {

    String getDisplayName(ProxiedPlayer player);

    String getName();

    static void loadProvider(UltimateParty plugin) {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();

        PartyNameProvider provider;
        if (pm.getPlugin("LuckPerms") != null) {
            provider = new LuckPermsProvider();
        } else if (pm.getPlugin("PowerfulPerms") != null) {
            provider = new PowerfulPermsProvider();
        } else if (pm.getPlugin("BungeePerms") != null) {
            provider = new BungeePermsProvider();
        } else {
            provider = null;
        }

        plugin.setDisplayNameProvider(provider);
    }

    static String addPrefixSuffix(ProxiedPlayer player, String prefix, String suffix) {
        return Strings.nullToEmpty(prefix) + player.getName() + Strings.nullToEmpty(suffix);
    }
}
