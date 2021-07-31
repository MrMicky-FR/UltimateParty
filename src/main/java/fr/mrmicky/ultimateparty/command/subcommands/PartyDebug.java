package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyDebug extends PartyCommand {

    public PartyDebug(UltimateParty plugin) {
        super("debug", plugin);
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        player.sendMessage(ChatUtils.colorToComponents("&bUltimateParty &7v&b" + getPlugin().getDescription().getVersion() + "&7by &bMrMicky&7."));
        player.sendMessage(ChatUtils.colorToComponents("&7ID:&b %%__USER__%%" + "/" + "%%__NONCE__%%"));
        player.sendMessage(ChatUtils.colorToComponents("&7Download:&b https://www.spigotmc.org/resources/ultimateparty.51548/"));
    }
}
