package fr.mrmicky.ultimateparty;

import fr.mrmicky.ultimateparty.command.subcommands.PartyChat;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.utils.PartyUtils;
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

    private UltimateParty m;
    private List<String> chatPrefixes;

    public PartyListener(UltimateParty m) {
        this.m = m;
        chatPrefixes = m.getConfig().getBoolean("ChatPrefix.Enable") ? m.getConfig().getStringList("ChatPrefix.Prefix") : Collections.emptyList();
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        Party party = m.getPartyManager().getParty(p);
        if (party != null) {
            if (party.isLeader(p)) {
                party.getPlayers().forEach(ps -> ps.sendMessage(Message.PARTY_DISBAND.getAsComponenent(p.getName())));
                party.disband();
            } else {
                party.removePlayer(p);
                party.getPlayers().forEach(ps -> ps.sendMessage(Message.PARTY_LEFT_BROADCAST.getAsComponenent(p.getName())));
            }
        }
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {
        ProxiedPlayer p = e.getPlayer();
        Party party = m.getPartyManager().getParty(p);
        String server = p.getServer().getInfo().getName();
        if (party != null && party.isLeader(p) && m.isServerEnable(p)
                && !PartyUtils.containsIgnoreCase(m.getDisableAutoJoin(), server)) {
            for (ProxiedPlayer ps : party.getPlayers()) {
                if (ps.getServer() != p.getServer() && m.isServerEnable(ps)) {
                    m.connect(ps, p.getServer().getInfo());
                }
            }
        }
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        String msg = e.getMessage();
        for (String s : chatPrefixes) {
            if (msg.toLowerCase().startsWith(s.toLowerCase()) && e.getSender() instanceof ProxiedPlayer) {
                ProxiedPlayer p = (ProxiedPlayer) e.getSender();
                Party party = m.getPartyManager().getParty(p);
                if (!m.isServerEnable(p)) {
                    p.sendMessage(Message.DISABLE_SERVER_SELF.getAsComponenent());
                    return;
                } else if (party == null) {
                    p.sendMessage(Message.NO_PARTY.getAsComponenent());
                    return;
                }

                // Removing chat prefix
                msg = msg.substring(s.length());
                e.setCancelled(true);

                // Removing spaces before message
                while (msg.startsWith(" ")) {
                    msg = msg.substring(1);
                }

                PartyChat.sendMessage(p, party, msg, m);
            }
        }
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        String msg = e.getCursor().toLowerCase();
        String args[] = msg.split(" ");

        if (args.length == 0) {
            return;
        }

        for (String s : chatPrefixes) {
            if (msg.toLowerCase().startsWith(s.toLowerCase()) && e.getSender() instanceof ProxiedPlayer) {
                ProxiedPlayer p = (ProxiedPlayer) e.getSender();
                Party party = m.getPartyManager().getParty(p);
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
