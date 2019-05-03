package fr.mrmicky.ultimateparty;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class PartyManager {

    private final UltimateParty plugin;

    private final Set<Party> partys = new HashSet<>();

    public PartyManager(UltimateParty plugin) {
        this.plugin = plugin;
    }

    public Set<Party> getPartys() {
        return Collections.unmodifiableSet(partys);
    }

    protected Set<Party> getAllPartys() {
        return partys;
    }

    public Party getParty(ProxiedPlayer p) {
        if (p != null) {
            for (Party party : partys) {
                if (party.getPlayers().contains(p)) {
                    return party;
                }
            }
        }
        return null;
        // The player is not in a party
    }

    public boolean hasParty(ProxiedPlayer p) {
        return getParty(p) != null;
    }

    public Party createParty(ProxiedPlayer leader) {
        if (hasParty(leader)) {
            throw new IllegalStateException(leader.getName() + " is already in a party");
        }

        return new Party(plugin, leader);
    }

    public int getInvitationDelay() {
        int delay = plugin.getConfig().getInt("InvitationDelay");

        return delay > 0 ? delay : 60;
    }
}
