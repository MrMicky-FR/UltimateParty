package fr.mrmicky.ultimateparty;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;
import java.util.Set;

public final class PartyManager {

    private Set<Party> partys = new HashSet<>();

    private UltimateParty m;

    public PartyManager(UltimateParty m) {
        this.m = m;
    }

    public Set<Party> getPartys() {
        return ImmutableSet.copyOf(partys);
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
        Preconditions.checkArgument(leader != null, "leader");
        Preconditions.checkState(!hasParty(leader), leader.getName() + " is already in a party");

        return new Party(this, leader);
    }

    public int getInvitationDelay() {
        int delay = m.getConfig().getInt("InvitationDelay");

        return delay > 0 ? delay : 60;
    }
}
