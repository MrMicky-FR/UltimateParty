package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyLeave extends PartyCommand {

    public PartyLeave() {
        super("leave");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party1) {
        Party party = getPlugin().getPartyManager().getParty(player);

        if (party == null) {
            Message.NO_PARTY.send(player);
            return;
        }

        if (party.isLeader(player)) {
            Message.PARTY_DISBAND.send(party.getPlayers(), player.getName());

        } else {
            party.removePlayer(player);

            Message.PARTY_LEFT.send(player);

            Message.PARTY_LEFT_BROADCAST.send(party.getPlayers(), player.getName());
        }
    }
}
