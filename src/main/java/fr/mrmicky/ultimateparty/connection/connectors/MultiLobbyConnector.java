package fr.mrmicky.ultimateparty.connection.connectors;

import cz.gameteam.dakado.multilobby.MultiLobby;
import fr.mrmicky.ultimateparty.connection.PartyConnector;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MultiLobbyConnector implements PartyConnector {

    @Override
    public String getName() {
        return "MultiLobby";
    }

    @Override
    public void connect(ProxiedPlayer player, ServerInfo server) {
        MultiLobby.directJoin(player, server.getName());
    }
}
