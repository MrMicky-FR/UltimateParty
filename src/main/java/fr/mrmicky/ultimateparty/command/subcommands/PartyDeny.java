package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.stream.Collectors;

public class PartyDeny extends PartyCommand {

    public PartyDeny() {
        super("deny");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        if (args.length == 0) {
            Message.NO_PLAYER.send(player);
            return;
        }

        ProxiedPlayer p2 = ProxyServer.getInstance().getPlayer(args[0]);
        Party party2 = p2 != null ? getPartyManager().getParty(p2) : null;

        if (party2 == null || !party2.isInvited(player)) {
            Message.NO_INVITATION.send(player);
            return;
        }

        party2.removeInvitation(player);
        Message.INVITATION_DENIED.send(player, p2.getName());
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer player, String[] args, Party party) {
        if (args.length != 1) {
            return null;
        }

        return getPlugin().getPartyManager().getPartys().stream()
                .filter(p -> ChatUtils.startsWithIgnoreCase(p.getLeader().getName(), args[0]) && p.isInvited(player))
                .map(p -> p.getLeader().getName())
                .collect(Collectors.toList());
    }
}
