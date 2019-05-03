package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class PartyTp extends PartyCommand {

    public PartyTp() {
        super("tp");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        if (party != null) {
            if (!getPlugin().isServerEnable(party.getLeader())) {
                p.sendMessage(Message.DISABLE_SERVER.getAsComponenent());
                return;
            }

            getPlugin().connect(p, party.getLeader().getServer().getInfo());
        } else {
            p.sendMessage(Message.NO_PARTY.getAsComponenent());
        }
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
        return null;
    }
}
