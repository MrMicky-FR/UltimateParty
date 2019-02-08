package fr.mrmicky.ultimateparty;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Party {

    private PartyManager pm;

    private ProxiedPlayer leader;
    private Set<ProxiedPlayer> players = new HashSet<>();
    private Set<UUID> invitations = new HashSet<>();

    protected Party(PartyManager pm, ProxiedPlayer leader) {
        this.pm = pm;
        this.leader = leader;
        this.players.add(leader);
        pm.getAllPartys().add(this);
    }

    public void disband() {
        players.clear();
        pm.getAllPartys().remove(this);
    }

    public Set<ProxiedPlayer> getPlayers() {
        return players;
    }

    public void addPlayer(ProxiedPlayer p) {
        players.add(p);
        invitations.remove(p.getUniqueId());
    }

    public void removePlayer(ProxiedPlayer p) {
        if (isLeader(p)) {
            disband();
        } else {
            players.remove(p);
        }
    }

    public ProxiedPlayer getLeader() {
        return leader;
    }

    public boolean isLeader(ProxiedPlayer p) {
        return p == leader;
    }

    public void setLeader(ProxiedPlayer p) {
        leader = p;
    }

    public void invite(ProxiedPlayer p) {
        if (!invitations.contains(p.getUniqueId())) {
            invitations.add(p.getUniqueId());

            ProxyServer.getInstance().getScheduler().schedule(UltimateParty.getInstance(),
                    () -> invitations.remove(p.getUniqueId()), pm.getInvitationDelay(), TimeUnit.SECONDS);
        }
    }

    public void removeInvitation(ProxiedPlayer p) {
        invitations.remove(p.getUniqueId());
    }

    public boolean isInvited(ProxiedPlayer p) {
        return invitations.contains(p.getUniqueId());
    }

    public boolean isFull() {
        return getMaxSize() < players.size();
    }

    public int getMaxSize() {
        Configuration groups = UltimateParty.getInstance().getConfig().getSection("MaxPartySize.Groups");
        for (String s : groups.getKeys()) {
            Configuration group = groups.getSection(s);
            if (leader.hasPermission(group.getString("Permission"))) {
                return group.getInt("Size");
            }
        }

        return UltimateParty.getInstance().getConfig().getInt("MaxPartySize.Default");
    }

    @Override
    public String toString() {
        return "Party{leader=" + leader.getName() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Party)) {
            return false;
        }

        Party party = (Party) o;
        return Objects.equals(leader, party.leader) && Objects.equals(players, party.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leader, players);
    }
}
