package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PartyLead extends PartyCommand {

    public PartyLead() {
        super("lead");
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
                party.setLeader(p2);
                party.getPlayers().forEach(ps -> ps.sendMessage(Message.NEW_LEADER.getAsComponenent(p2.getName())));
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
        for (ProxiedPlayer ps : party.getPlayers()) {
            if (ps != p && ps.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                members.add(ps.getName());
            }
        }
        return members;
    }
}
