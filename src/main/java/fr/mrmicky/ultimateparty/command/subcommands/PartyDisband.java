package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class PartyDisband extends PartyCommand {

    public PartyDisband() {
        super("disband");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        if (party != null && party.isLeader(p)) {
            party.getPlayers().forEach(ps -> ps.sendMessage(Message.PARTY_DISBAND.getAsComponenent(p.getName())));
            party.disband();
        } else {
            p.sendMessage(Message.NO_PARTY.getAsComponenent());
        }
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
        return null;
    }
}
