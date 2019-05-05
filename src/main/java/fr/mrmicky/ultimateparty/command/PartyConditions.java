package fr.mrmicky.ultimateparty.command;

import fr.mrmicky.ultimateparty.Party;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author MrMicky
 */
public final class PartyConditions {

    private PartyConditions() {
        throw new UnsupportedOperationException();
    }

    public static void checkHasParty(Party party, ProxiedPlayer player) {
        if (party == null) {

            throw PartySilentCommandException.INSTANCE;
        }
    }

    public static void checkHasNotParty(Party party, ProxiedPlayer player) {
        if (party != null) {

            throw PartySilentCommandException.INSTANCE;
        }
    }

    public static void checkIsLeader(Party party, ProxiedPlayer player) {
        if (party == null || !party.isLeader(player)) {

            throw PartySilentCommandException.INSTANCE;
        }
    }
}
