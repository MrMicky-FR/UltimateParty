package fr.mrmicky.ultimateparty;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class PartyManager {

    private Set<Party> partys = new HashSet<>();

    protected PartyManager() {
    }

    public Set<Party> getPartys() {
        return Collections.unmodifiableSet(partys);
        // Partys list should not be modify directly with the api
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
            throw new IllegalStateException("This player is already in a party.");
        }

        if (leader == null) {
            throw new IllegalArgumentException("Leader cannot be null");
        }

        return new Party(this, leader);
    }

    public static int getInvitationDelay() {
        int delay = UltimateParty.getInstance().getConfig().getInt("InvitationDelay");

        return delay > 0 ? delay : 60;
    }
}
