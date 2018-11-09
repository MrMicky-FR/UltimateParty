package fr.mrmicky.ultimateparty.command.subcommands;

import java.util.List;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.options.PartyOption;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyJoin extends PartyCommand {

	public PartyJoin() {
		super("join");
	}

	@Override
	public void execute(ProxiedPlayer p, String[] args, Party party) {
		if (party == null) {
			if (args.length == 0) {
				p.sendMessage(Message.NO_PLAYER.getAsComponenent());
				return;
			}
			ProxiedPlayer p2 = ProxyServer.getInstance().getPlayer(args[0]);
			Party party2 = getPartyManager().getParty(p2);
			if (party2 != null) {
				if (!m.getDataManager().getOption(party2.getLeader(), PartyOption.PUBLIC_PARTY)) {
					p.sendMessage(Message.PARTY_PRIVATE.getAsComponenent());
					return;
				}
				if (!party2.isFull()) {
					party2.addPlayer(p);
					party2.getPlayers()
							.forEach(ps -> ps.sendMessage(Message.PLAYER_JOIN.getAsComponenent(p.getName())));
				} else {
					p.sendMessage(Message.PARTY_FULL.getAsComponenent());
				}
			} else {
				p.sendMessage(Message.PARTY_NOT_EXISTS.getAsComponenent());
			}
		} else {
			p.sendMessage(Message.ALREADY_IN_PARTY_SELF.getAsComponenent());
		}
	}

	@Override
	public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
		return null;
	}
}
