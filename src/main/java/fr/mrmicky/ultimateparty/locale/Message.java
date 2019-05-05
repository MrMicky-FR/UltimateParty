package fr.mrmicky.ultimateparty.locale;

import fr.mrmicky.ultimateparty.utils.ChatUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public enum Message {

    /*
     *  Prefix
     */
    PREFIX("&6Party \u279C &f", false),

    /*
     * Errors
     */
    NO_PERMISSION("&cYou do not have permission to use this command!", true),
    //NO_CONSOLE("&cConsole can't use party commands", true),

    NO_PLAYER("&cYou must indicate a player", true),

    UNKNOW_SUBCOMMAND("&cUnknown subcommand, do /party", true),
    ALREADY_CONNECT("&cYou are already connected to that server.", true),

    PLAYER_NOT_FOUND("&cThis player cannot be found.", true),
    PLAYER_NO_IN_PARTY("&cThis player is not in the party", true),

    ALREADY_IN_PARTY("&cThis player is already in a party.", true),
    ALREADY_IN_PARTY_SELF("&cYou are already in a party.", true),

    DISABLE_SERVER("&cThis player is on a disabled server.", true),
    DISABLE_SERVER_SELF("&cPartys are disabled on this server.", true),

    PARTY_FULL("&cThis party is full", true),
    PARTY_FULL_SELF("&cThe party is full", true),

    NO_PARTY("&cYou are not in a party.", true),

    PARTY_NOT_EXISTS("&cThis party don't exist", true),
    // v 1.1
    PARTY_PRIVATE("&cThis party is private", true),
    DISABLE_INVITATION("&cThis player has disabled party invitations", true),
    //DISABLE_INVITATION_SELF("&cYou have disabled party invitations", true), // Remove in 1.2.2

    NO_LEADER("&cYou are not the leader of a party.", true),
    NO_INVITATION("&cThis invitation doesn't exist", true),
    INVITATION_ALREADY_SEND("&cYou already invited this player.", true),

    NO_URL_CHAT("&cYou can't put url in the chat", true),

    /*
     *  Success
     */

    PARTY_CREATE("&fThe party was created.", true),
    PARTY_LEFT("&fYou left the party.", true),
    PARTY_LEFT_BROADCAST("&b{0} &fleft the party.", true),

    PLAYER_JOIN("&b{0} &fjoined the party.", true),

    PLAYER_KICK("&fYou have been kicked out of the party by &b{0}&f.", true),
    PLAYER_KICK_BROADCAST("&b{0} &fwas kicked out of the party by {1}&f.", true),

    PARTY_LIST("Members:", true),
    PARTY_LIST_FORMAT_LEADER("&c* {0} (Leader) &7: {1}", false),
    PARTY_LIST_FORMAT("&6* {0} &7: {1}", false),

    NEW_LEADER("&b{0} &fis now the leader of the party.", true),

    PARTY_DISBAND("&fThe party has been disbanded by &b{0}&f.", true),

    INVITATION_SEND("&fThe invitation has been sent to &b{0}&f.", true),
    INVITATION_RECEIVE("&b{0} &finvited you to join his party: &9[{0-}&9] [{1-}&9]. &fYou have {1} seconds to accept", true),

    INVITATION_DENIED("&fYou denied {0}'s invitation", true),

    /*
     * Menu
     */

    MENU_NO_PARTY("&9[{0-}&9] [{1-}&9]", false),
    MENU_PARTY_MEMBER("&9[{0-}&9] [{1-}&9] [{2-}&9] [{3-}&9]", false),
    MENU_PARTY_LEADER("&9[{0-}&9] [{1-}&9] [{2-}&9] [{3-}&9]", false),

    PARTY_FORMAT_LEADER("&c{0} (Leader)", false),
    PARTY_FORMAT("&7{0}", false),

    PARTY_FORMAT_WHEN_LEADER("&9[{0-} {1-}&9] &7{0}", false),

    OPTION_FORMAT("&9[{0-}&9] &7{0}", false),

    /*
     * Random
     */

    CHAT_FORMAT("&b{0}: &f", false),
    CHAT_FORMAT_HOVER("&fClick to send a message to the party", false),

    /*
     * Buttons
     */

    SPACER_TOP("&9&m---------------------------------", false),
    SPACER_BOTTOM("&9&m---------------------------------", false),

    INVITATION_ACCEPT_BUTTON("&aAccept", false),
    INVITATION_ACCEPT_BUTTON_HOVER("&aAccept and join the party", false),

    INVITATION_DENY_BUTTON("&cDeny", false),
    INVITATION_DENY_BUTTON_HOVER("&cDeny this invitation", false),

    CREATE_BUTTON("&2Create", false),
    CREATE_BUTTON_HOVER("&2Create a party", false),

    INVITE_BUTTON("&2Invite", false),
    INVITE_BUTTON_HOVER("&2Invite a player in the party", false),

    CHAT_BUTTON("&dChat", false),
    CHAT_BUTTON_HOVER("&dSend a message to the party", false),

    JOIN_BUTTON("&bJoin", false),
    JOIN_BUTTON_HOVER("&bJoin the server where the party leader is", false),

    KICK_BUTTON("&c\u2716", false),
    KICK_BUTTON_HOVER("&cKick {0} from the party", false),

    LEAD_BUTTON("&a\u271A", false),
    LEAD_BUTTON_HOVER("&aPromote {0} as party leader", false),

    LEAVE_BUTTON("&cLeave", false),
    LEAVE_BUTTON_HOVER("&cLeave the party", false),

    DISBAND_BUTTON("&cDisband", false),
    DISBAND_BUTTON_HOVER("&cDisband the party", false),

    OPTIONS_BUTTON("&eOptions", false),
    OPTIONS_BUTTON_HOVER("&eConfigure party options", false),

    /*
     * Options - v 1.1
     */

    OPTION_ENABLE_BUTTON("&a\u2714", false),
    OPTION_ENABLE_BUTTON_HOVER("&aEnable this option", false),

    OPTION_DISABLE_BUTTON("&c\u2716", false),
    OPTION_DISABLE_BUTTON_HOVER("&cDisable this option", false),

    OPTION_PARTY_INVITATION("Receive party invitations", false),
    OPTION_PUBLIC_PARTY("Anyone can join the party", false);

    private String message;
    private boolean prefix;

    Message(String message, boolean prefix) {
        this.message = ChatUtils.color(message);
        this.prefix = prefix;
    }

    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    public String getAsString(Object... objects) {
        String s = message;
        for (int i = 0; i < objects.length; i++) {
            s = s.replace("{" + i + "}", String.valueOf(objects[i]));
        }
        return (prefix ? PREFIX.message : "") + s;
    }

    public BaseComponent[] getAsComponent(Object... objects) {
        return TextComponent.fromLegacyText(getAsString(objects));
    }

    public void send(ProxiedPlayer p, Object... objects) {
        p.sendMessage(getAsComponent(objects));
    }

    public void send(Iterable<ProxiedPlayer> players, Object... objects) {
        BaseComponent[] components = getAsComponent(objects);

        players.forEach(p -> p.sendMessage(components));
    }
}
