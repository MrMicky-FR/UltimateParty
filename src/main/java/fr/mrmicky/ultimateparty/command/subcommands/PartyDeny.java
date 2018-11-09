package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PartyDeny extends PartyCommand {

    public PartyDeny() {
        super("deny");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        if (args.length == 0) {
            p.sendMessage(Message.NO_PLAYER.getAsComponenent());
            return;
        }
        ProxiedPlayer p2 = ProxyServer.getInstance().getPlayer(args[0]);
        Party party2 = p2 == null ? null : getPartyManager().getParty(p2);
        if (party2 != null && party2.isInvited(p)) {
            party2.removeInvitation(p);
            p.sendMessage(Message.INVITATION_DENIED.getAsComponenent(p2.getName()));
        } else {
            p.sendMessage(Message.NO_INVITATION.getAsComponenent());
        }
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
        if (args.length != 1) {
            return null;
        }

        List<String> invitations = new ArrayList<>();

        for (Party pa : m.getPartyManager().getPartys()) {
            if (pa.getLeader().getName().toLowerCase().startsWith(args[0].toLowerCase()) && pa.isInvited(p)) {
                invitations.add(pa.getLeader().getName());
            }
        }
        return invitations;
    }
}
