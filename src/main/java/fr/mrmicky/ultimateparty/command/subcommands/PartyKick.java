package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PartyKick extends PartyCommand {

    public PartyKick() {
        super("kick");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        if (party != null && party.isLeader(p)) {
            if (args.length == 0) {
                p.sendMessage(Message.NO_PLAYER.getAsComponenent());
                return;
            }
            ProxiedPlayer p2 = m.getProxy().getPlayer(args[0]);
            if (p2 != null && party.getPlayers().contains(p2) && p != p2) {
                p2.sendMessage(Message.PLAYER_KICK.getAsComponenent(p.getName()));
                party.removePlayer(p2);
                party.getPlayers().forEach(ps -> ps
                        .sendMessage(Message.PLAYER_KICK_BROADCAST.getAsComponenent(p2.getName(), p.getName())));
            } else {
                p.sendMessage(Message.PLAYER_NO_IN_PARTY.getAsComponenent());
            }
        } else {
            p.sendMessage(Message.NO_LEADER.getAsComponenent());
        }
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
        if (args.length != 1) {
            return null;
        }

        List<String> members = new ArrayList<>();
        party.getPlayers().forEach(ps -> {
            if (ps != p && ps.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                members.add(ps.getName());
            }
        });
        return members;
    }
}
