package fr.mrmicky.ultimateparty.listeners;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.command.subcommands.PartyChat;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.utils.StringUtils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PartyListener implements Listener {

    private final UltimateParty plugin;

    public PartyListener(UltimateParty plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        Party party = plugin.getPartyManager().getParty(player);

        if (party == null) {
            return;
        }

        if (party.isLeader(player)) {
            Message.PARTY_DISBAND.send(party.getPlayers(), player.getName());
            plugin.getPartyManager().disbandParty(party);
        } else {
            party.removePlayer(player);
            Message.PARTY_LEFT_BROADCAST.send(party.getPlayers(), player.getName());
        }
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent e) {
        ProxiedPlayer player = e.getPlayer();
        Party party = plugin.getPartyManager().getParty(player);
        ServerInfo server = player.getServer().getInfo();

        if (party == null || !party.isLeader(player) || !plugin.isServerEnable(player)) {
            return;
        }

        if (StringUtils.containsIgnoreCase(plugin.getConfig().getStringList("DisableAutoJoinServers"), server.getName())) {
            return;
        }

        for (ProxiedPlayer ps : party.getPlayers()) {
            if (!ps.getServer().equals(player.getServer()) && plugin.isServerEnable(ps)) {
                plugin.connect(ps, server);
            }
        }
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        if (!(e.getSender() instanceof ProxiedPlayer) || !plugin.getConfig().getBoolean("ChatPrefix.Enable")) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        String message = e.getMessage();

        for (String prefix : plugin.getConfig().getStringList("ChatPrefix.Prefix")) {
            if (!StringUtils.startsWithIgnoreCase(message, prefix)) {
                continue;
            }

            Party party = plugin.getPartyManager().getParty(player);

            if (party == null) {
                Message.NO_PARTY.send(player);
                return;
            }

            if (!plugin.isServerEnable(player)) {
                Message.DISABLE_SERVER_SELF.send(player);
                return;
            }

            e.setCancelled(true);
            PartyChat.sendMessage(player, party, message.substring(prefix.length()).trim(), plugin);
        }
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        if (!(e.getSender() instanceof ProxiedPlayer) || !plugin.getConfig().getBoolean("ChatPrefix.Enable")) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        String cursor = e.getCursor();
        String[] args = cursor.split(" ");

        if (args.length == 0) {
            return;
        }

        for (String prefix : plugin.getConfig().getStringList("ChatPrefix.Prefix")) {
            if (!StringUtils.startsWithIgnoreCase(cursor, prefix)) {
                continue;
            }

            Party party = plugin.getPartyManager().getParty(player);
            if (party == null) {
                continue;
            }

            String lastArg = args[args.length - 1];

            party.getPlayers().stream()
                    .filter(ps -> StringUtils.startsWithIgnoreCase(ps.getName(), lastArg))
                    .forEach(ps -> e.getSuggestions().add(ps.getName()));
            break;
        }
    }
}
