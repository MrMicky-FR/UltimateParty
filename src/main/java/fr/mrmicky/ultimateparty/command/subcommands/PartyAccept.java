package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PartyAccept extends PartyCommand {

    public PartyAccept() {
        super("accept");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        if (party == null) {
            if (args.length == 0) {
                p.sendMessage(Message.NO_PLAYER.getAsComponenent());
                return;
            }
            ProxiedPlayer p2 = ProxyServer.getInstance().getPlayer(args[0]);
            Party party2 = (p2 == null ? null : getPartyManager().getParty(p2));
            if (party2 != null && party2.isInvited(p)) {
                if (!party2.isFull()) {
                    party2.addPlayer(p);
                    party2.getPlayers()
                            .forEach(ps -> ps.sendMessage(Message.PLAYER_JOIN.getAsComponenent(p.getName())));
                } else {
                    p.sendMessage(Message.PARTY_FULL.getAsComponenent());
                }
            } else {
                p.sendMessage(Message.NO_INVITATION.getAsComponenent());
            }
        } else {
            p.sendMessage(Message.ALREADY_IN_PARTY_SELF.getAsComponenent());
        }
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
        if (args.length != 1) {
            return null;
        }

        List<String> invits = new ArrayList<>();
        for (Party pa : m.getPartyManager().getPartys()) {
            if (pa.getLeader().getName().toLowerCase().startsWith(args[0].toLowerCase()) && pa.isInvited(p)) {
                invits.add(pa.getLeader().getName());
            }
        }
        return invits;
    }
}
