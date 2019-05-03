package fr.mrmicky.ultimateparty.command;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.PartyManager;
import fr.mrmicky.ultimateparty.UltimateParty;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collections;
import java.util.List;

public abstract class PartyCommand {

    private final String name;
    private final UltimateParty plugin = UltimateParty.getInstance();

    public PartyCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UltimateParty getPlugin() {
        return plugin;
    }

    public PartyManager getPartyManager() {
        return plugin.getPartyManager();
    }

    public abstract void execute(ProxiedPlayer player, String[] args, Party party);

    public List<String> onTabComplete(ProxiedPlayer player, String[] args, Party party) {
        return Collections.emptyList();
    }
}
