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

    private final UltimateParty plugin;

    private final Set<ProxiedPlayer> players = new HashSet<>();
    private final Set<UUID> invitations = new HashSet<>();

    private ProxiedPlayer leader;

    Party(ProxiedPlayer leader, UltimateParty plugin) {
        this.leader = Objects.requireNonNull(leader, "leader");
        this.plugin = plugin;

        players.add(leader);
    }

    public Set<ProxiedPlayer> getPlayers() {
        return players;
    }

    public void addPlayer(ProxiedPlayer player) {
        players.add(player);
        invitations.remove(player.getUniqueId());
    }

    public void removePlayer(ProxiedPlayer player) {
        if (isLeader(player)) {
            throw new IllegalStateException("Cannot remove party leader");
        }

        players.remove(player);
    }

    public ProxiedPlayer getLeader() {
        return leader;
    }

    public void setLeader(ProxiedPlayer leader) {
        this.leader = Objects.requireNonNull(leader, "leader");
    }

    public boolean isLeader(ProxiedPlayer player) {
        return leader.equals(player);
    }

    public void createInvitation(ProxiedPlayer player) {
        if (invitations.add(player.getUniqueId())) {
            ProxyServer.getInstance().getScheduler().schedule(plugin, () ->
                    invitations.remove(player.getUniqueId()), plugin.getPartyManager().getInvitationDelay(), TimeUnit.SECONDS);
        }
    }

    public void removeInvitation(ProxiedPlayer player) {
        invitations.remove(player.getUniqueId());
    }

    public boolean isInvited(ProxiedPlayer player) {
        return invitations.contains(player.getUniqueId());
    }

    public boolean isFull() {
        return getMaxSize() < players.size();
    }

    public int getMaxSize() {
        Configuration groups = plugin.getConfig().getSection("MaxPartySize.Groups");

        for (String s : groups.getKeys()) {
            Configuration group = groups.getSection(s);
            if (leader.hasPermission(group.getString("Permission"))) {
                return group.getInt("Size");
            }
        }

        return plugin.getConfig().getInt("MaxPartySize.Default");
    }

    @Override
    public String toString() {
        return "Party{leader=" + leader + ", players=" + players.size() + ", invitations=" + invitations.size() + '}';
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
        return Objects.equals(leader, party.leader) && players.equals(party.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leader, players);
    }
}
