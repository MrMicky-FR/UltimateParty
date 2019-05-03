package fr.mrmicky.ultimateparty.connection;

import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.connection.connectors.MultiLobbyConnector;
import fr.mrmicky.ultimateparty.connection.connectors.PlayerBalancerConnector;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;

public interface PartyConnector {

    void connect(ProxiedPlayer pp, ServerInfo server);

    String getName();

    static void loadConnector(UltimateParty plugin) {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();

        PartyConnector connector;
        if (pm.getPlugin("MultiLobby") != null) {
            connector = new MultiLobbyConnector();
        } else if (pm.getPlugin("PlayerBalancer") != null) {
            connector = new PlayerBalancerConnector();
        } else {
            connector = null;
        }

        plugin.setConnector(connector);
    }
}
