package fr.mrmicky.ultimateparty;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PartyManager {

    private final List<Party> partys = new ArrayList<>();

    private final UltimateParty plugin;

    public PartyManager(UltimateParty plugin) {
        this.plugin = plugin;
    }

    public Collection<Party> getPartys() {
        return Collections.unmodifiableList(partys);
    }

    public Party getParty(ProxiedPlayer player) {
        if (player != null) {
            for (Party party : partys) {
                if (party.hasPlayer(player)) {
                    return party;
                }
            }
        }
        return null;
    }

    public boolean hasParty(ProxiedPlayer player) {
        return getParty(player) != null;
    }

    public Party createParty(ProxiedPlayer leader) {
        if (hasParty(leader)) {
            throw new IllegalStateException(leader.getName() + " is already in a party");
        }

        Party party = new Party(leader, plugin);

        partys.add(party);

        return party;
    }

    public void disbandParty(Party party) {
        partys.remove(party);
    }

    public int getInvitationDelay() {
        int delay = plugin.getConfig().getInt("InvitationDelay");

        return delay > 0 ? delay : 60;
    }
}
