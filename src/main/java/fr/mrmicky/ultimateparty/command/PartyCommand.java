package fr.mrmicky.ultimateparty.command;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.PartyManager;
import fr.mrmicky.ultimateparty.UltimateParty;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public abstract class PartyCommand {

    private String name;
    protected UltimateParty m;

    public PartyCommand(String name) {
        this.name = name;

        m = UltimateParty.getInstance();
    }

    public String getName() {
        return name;
    }

    public PartyManager getPartyManager() {
        return m.getPartyManager();
    }

    public abstract void execute(ProxiedPlayer p, String[] args, Party party);

    public abstract List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party);
}
