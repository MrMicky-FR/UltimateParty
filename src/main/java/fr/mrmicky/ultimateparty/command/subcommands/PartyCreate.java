package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyCreate extends PartyCommand {

    public PartyCreate() {
        super("create");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        if (party != null) {
            Message.ALREADY_IN_PARTY_SELF.send(player);
            return;
        }

        getPlugin().getPartyManager().createParty(player);

        Message.PARTY_CREATE.send(player);

        getPlugin().openMenu(player);
    }
}
