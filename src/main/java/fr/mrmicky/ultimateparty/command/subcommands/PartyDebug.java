package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class PartyDebug extends PartyCommand {

    public PartyDebug() {
        super("debug");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        send(player);
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer player, String[] args, Party party) {
        send(player);
        return null;
    }

    private void send(ProxiedPlayer p) {
        p.sendMessage(ChatUtils.colorToComponents("&bUltimateParty &7v&b" + getPlugin().getDescription().getVersion() + "&7by &bMrMicky&7."));
        p.sendMessage(ChatUtils.colorToComponents("&7ID:&b %%__USER__%%" + "/" + "%%__NONCE__%%"));
        p.sendMessage(ChatUtils.colorToComponents("&7Download:&b https://www.spigotmc.org/resources/ultimateparty.51548/"));
    }
}
