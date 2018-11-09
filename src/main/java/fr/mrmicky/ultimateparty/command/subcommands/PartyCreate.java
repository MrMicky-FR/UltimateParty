package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class PartyCreate extends PartyCommand {

    public PartyCreate() {
        super("create");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        if (party == null) {
            getPartyManager().createParty(p);
            p.sendMessage(Message.PARTY_CREATE.getAsComponenent());
            m.openMenu(p);
        } else {
            p.sendMessage(Message.ALREADY_IN_PARTY_SELF.getAsComponenent());
        }
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
        return null;
    }
}
