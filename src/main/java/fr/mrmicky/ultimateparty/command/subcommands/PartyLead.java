package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.stream.Collectors;

public class PartyLead extends PartyCommand {

    public PartyLead() {
        super("lead");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
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

        party.setLeader(p2);

        Message.NEW_LEADER.send(party.getPlayers(), p2.getName());
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer player, String[] args, Party party) {
        if (args.length != 1 || party == null) {
            return null;
        }

        return party.getPlayers().stream()
                .filter(p -> !p.equals(player) && StringUtils.startsWithIgnoreCase(p.getName(), args[0]))
                .map(CommandSender::getName)
                .collect(Collectors.toList());
    }
}
