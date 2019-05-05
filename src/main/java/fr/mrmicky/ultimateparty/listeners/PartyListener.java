package fr.mrmicky.ultimateparty.listeners;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.command.subcommands.PartyChat;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Collections;
import java.util.List;

public class PartyListener implements Listener {

    private final UltimateParty plugin;
    private List<String> chatPrefixes;

    public PartyListener(UltimateParty plugin) {
        this.plugin = plugin;

        chatPrefixes = plugin.getConfig().getBoolean("ChatPrefix.Enable") ? plugin.getConfig().getStringList("ChatPrefix.Prefix") : Collections.emptyList();
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        Party party = plugin.getPartyManager().getParty(p);

        if (party == null) {
            return;
        }

        if (party.isLeader(p)) {
            Message.PARTY_DISBAND.send(party.getPlayers(), p.getName());
            plugin.getPartyManager().disbandParty(party);
        } else {
            Message.PARTY_LEFT_BROADCAST.send(party.getPlayers(), p.getName());
            party.removePlayer(p);
        }
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {
        ProxiedPlayer p = e.getPlayer();
        Party party = plugin.getPartyManager().getParty(p);
        ServerInfo server = p.getServer().getInfo();

        if (party == null || !party.isLeader(p) || !plugin.isServerEnable(p)) {
            return;
        }

        if (ChatUtils.containsIgnoreCase(plugin.getDisableAutoJoin(), server.getName())) {
            return;
        }

        for (ProxiedPlayer ps : party.getPlayers()) {
            if (!ps.getServer().equals(p.getServer()) && plugin.isServerEnable(ps)) {
                plugin.connect(ps, server);
            }
        }
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        if (!(e.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) e.getSender();
        String msg = e.getMessage();

        for (String s : chatPrefixes) {
            if (msg.toLowerCase().startsWith(s.toLowerCase())) {
                Party party = plugin.getPartyManager().getParty(p);
                if (!plugin.isServerEnable(p)) {
                    Message.DISABLE_SERVER_SELF.send(p);
                    return;
                } else if (party == null) {
                    Message.NO_PARTY.send(p);
                    return;
                }

                // Removing chat prefix
                msg = msg.substring(s.length()).trim();
                e.setCancelled(true);

                PartyChat.sendMessage(p, party, msg, plugin);
            }
        }
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        if (!(e.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) e.getSender();
        String msg = e.getCursor().toLowerCase();
        String[] args = msg.split(" ");

        if (args.length == 0) {
            return;
        }

        for (String s : chatPrefixes) {
            if (msg.toLowerCase().startsWith(s.toLowerCase())) {
                Party party = plugin.getPartyManager().getParty(p);
                if (party != null) {
                    for (ProxiedPlayer ps : party.getPlayers()) {
                        if (ps.getName().toLowerCase().startsWith(args[args.length - 1])) {
                            e.getSuggestions().add(ps.getName());
                        }
                    }
                }
                break;
            }
        }
    }
}
