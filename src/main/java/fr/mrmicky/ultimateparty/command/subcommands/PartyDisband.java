package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyDisband extends PartyCommand {

    public PartyDisband() {
        super("disband");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        if (party == null || !party.isLeader(player)) {
            Message.NO_PARTY.send(player);
            return;
        }

        Message.PARTY_DISBAND.send(party.getPlayers(), player.getName());

        getPartyManager().disbandParty(party);
    }
}
