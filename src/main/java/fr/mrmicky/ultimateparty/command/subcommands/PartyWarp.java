package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyWarp extends PartyCommand {

    public PartyWarp() {
        super("warp");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        if (party == null || !party.isLeader(player)) {
            Message.NO_LEADER.send(player);
            return;
        }

        if (!getPlugin().isServerEnable(party.getLeader())) {
            Message.DISABLE_SERVER.send(player);
            return;
        }

        ServerInfo server = player.getServer().getInfo();

        party.getPlayers().stream()
                .filter(p -> !p.getServer().equals(player.getServer()) && getPlugin().isServerEnable(p))
                .forEach(p -> getPlugin().connect(p, server));

        Message.PLAYERS_TELEPORTED.send(player);
    }
}
