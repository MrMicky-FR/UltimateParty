package fr.mrmicky.ultimateparty.command.subcommands;

import java.util.List;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyDebug extends PartyCommand {

	public PartyDebug() {
		super("debug");
	}

	@Override
	public void execute(ProxiedPlayer p, String[] args, Party party) {
		send(p);
	}

	private void send(ProxiedPlayer p) {
		p.sendMessage(TextComponent.fromLegacyText("§bUltimateParty §7v §b" + m.getDescription().getVersion()
				+ "§7. ID:§b %%__USER__%% §7, NONCE:§b %%__NONCE__%%"));
		p.sendMessage(TextComponent.fromLegacyText("§7Download: §bhttps://www.spigotmc.org/resources/ultimateparty.51548/"));
	}

	@Override
	public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
		send(p);
		return null;
	}
}
