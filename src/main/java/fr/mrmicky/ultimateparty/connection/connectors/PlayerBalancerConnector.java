package fr.mrmicky.ultimateparty.connection.connectors;

import com.jaimemartz.playerbalancer.manager.PlayerLocker;
import fr.mrmicky.ultimateparty.connection.PartyConnector;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerBalancerConnector implements PartyConnector {

    @Override
    public String getName() {
        return "PlayerBalancer";
    }

    @Override
    public void connect(ProxiedPlayer p, ServerInfo server) {
        // Maybe a better way to do that ?
        PlayerLocker.lock(p);
        p.connect(server);
        PlayerLocker.unlock(p);
    }
}
