package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class PartyDebug extends PartyCommand {

    public PartyDebug() {
        super("debug");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        send(p);
    }

    private void send(ProxiedPlayer p) {
        String ver = m.getDescription().getVersion();
        p.sendMessage(TextComponent.fromLegacyText(ChatUtils.color("&bUltimateParty &7v&b" + ver + "&7.")));
        p.sendMessage(TextComponent.fromLegacyText(ChatUtils.color("&7ID:&b %%__USER__%%" + "/" + "%%__NONCE__%%")));
        p.sendMessage(TextComponent.fromLegacyText(ChatUtils.color("&7Download: &bhttps://www.spigotmc.org/resources/ultimateparty.51548/")));
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
        send(p);
        return null;
    }
}
