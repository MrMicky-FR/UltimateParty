package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.stream.Collectors;

public class PartyKick extends PartyCommand {

    public PartyKick() {
        super("kick");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party1) {
        Party party = getPlugin().getPartyManager().getParty(player);

        if (party == null || !party.isLeader(player)) {
            Message.NO_LEADER.send(player);
            return;
        }

        if (args.length == 0) {
            Message.NO_PLAYER.send(player);
            return;
        }

        ProxiedPlayer p2 = getPlugin().getProxy().getPlayer(args[0]);

        if (p2 == null || !party.getPlayers().contains(p2) || player.equals(p2)) {
            Message.PLAYER_NO_IN_PARTY.send(player);
            return;
        }

        party.removePlayer(p2);

        Message.PLAYER_KICK.send(p2, player.getName());

        Message.PLAYER_KICK_BROADCAST.send(party.getPlayers(), p2.getName(), player.getName());
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer player, String[] args, Party party) {
        if (args.length != 1) {
            return null;
        }

        return party.getPlayers().stream()
                .filter(p -> !p.equals(player) && ChatUtils.startsWithIgnoreCase(p.getName(), args[0]))
                .map(CommandSender::getName)
                .collect(Collectors.toList());
    }
}
