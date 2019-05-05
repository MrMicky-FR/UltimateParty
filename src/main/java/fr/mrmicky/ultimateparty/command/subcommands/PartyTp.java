package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyTp extends PartyCommand {

    public PartyTp() {
        super("tp");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        if (party == null) {
            Message.NO_PARTY.send(player);
            return;
        }

        if (!getPlugin().isServerEnable(party.getLeader())) {
            Message.DISABLE_SERVER.send(player);
            return;
        }

        getPlugin().connect(player, party.getLeader().getServer().getInfo());
    }
}
