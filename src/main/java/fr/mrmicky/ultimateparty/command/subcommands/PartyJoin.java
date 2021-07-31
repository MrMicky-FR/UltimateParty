package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.options.PartyOption;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyJoin extends PartyCommand {

    public PartyJoin(UltimateParty plugin) {
        super("join", plugin);
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        if (party != null) {
            Message.ALREADY_IN_PARTY_SELF.send(player);
            return;
        }

        if (args.length == 0) {
            Message.NO_PLAYER.send(player);
            return;
        }

        ProxiedPlayer p2 = ProxyServer.getInstance().getPlayer(args[0]);
        Party party2 = getPartyManager().getParty(p2);

        if (party2 == null) {
            Message.PARTY_NOT_EXISTS.send(player);
            return;
        }

        if (!getPlugin().getDataManager().isOptionEnabled(party2.getLeader(), PartyOption.PUBLIC_PARTY)) {
            Message.PARTY_PRIVATE.send(player);
            return;
        }

        if (party2.isFull()) {
            Message.PARTY_FULL.send(player);
            return;
        }

        party2.addPlayer(player);

        Message.PLAYER_JOIN.send(party2.getPlayers(), player.getName());
    }
}
