package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class PartyList extends PartyCommand {

    public PartyList() {
        super("list");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        if (party != null) {
            p.sendMessage(Message.PARTY_LIST.getAsComponenent());
            p.sendMessage(Message.PARTY_LIST_FORMAT_LEADER.getAsComponenent(party.getLeader().getName(),
                    party.getLeader().getServer().getInfo().getName()));
            party.getPlayers().forEach(ps -> {
                if (!party.isLeader(ps)) {
                    p.sendMessage(Message.PARTY_LIST_FORMAT.getAsComponenent(ps.getName(), ps.getServer().getInfo().getName()));
                }
            });
        } else {
            p.sendMessage(Message.NO_PARTY.getAsComponenent());
        }
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
        return null;
    }
}
