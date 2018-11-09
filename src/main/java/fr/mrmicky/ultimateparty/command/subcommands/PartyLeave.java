package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class PartyLeave extends PartyCommand {

    public PartyLeave() {
        super("leave");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        if (party != null) {
            if (party.isLeader(p)) {
                party.getPlayers().forEach(ps -> ps.sendMessage(Message.PARTY_DISBAND.getAsComponenent(p.getName())));
            } else {
                party.removePlayer(p);
                p.sendMessage(Message.PARTY_LEFT.getAsComponenent());
                party.getPlayers().forEach(ps -> ps.sendMessage(Message.PARTY_LEFT_BROADCAST.getAsComponenent(p.getName())));
            }
        }
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
        return null;
    }
}
