package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyList extends PartyCommand {

    public PartyList() {
        super("list");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        if (party == null) {
            Message.NO_PARTY.send(player);
            return;
        }

        Message.PARTY_LIST.send(player);
        Message.PARTY_LIST_FORMAT_LEADER.send(player, party.getLeader().getName(), party.getLeader().getServer().getInfo().getName());

        for (ProxiedPlayer ps : party.getPlayers()) {
            if (!party.isLeader(ps)) {
                Message.PARTY_LIST_FORMAT.send(player, ps.getName(), ps.getServer().getInfo().getName());
            }
        }

    }
}
