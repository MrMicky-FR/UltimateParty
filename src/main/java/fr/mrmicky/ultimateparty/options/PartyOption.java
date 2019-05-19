package fr.mrmicky.ultimateparty.options;

import fr.mrmicky.ultimateparty.locale.Message;

public enum PartyOption {

    RECEIVE_INVITATIONS(true, Message.OPTION_PARTY_INVITATION),
    PUBLIC_PARTY(false, Message.OPTION_PUBLIC_PARTY);

    private final boolean defaultValue;
    private final Message message;

    PartyOption(boolean defaultValue, Message message) {
        this.defaultValue = defaultValue;
        this.message = message;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public Message getMessage() {
        return message;
    }
}
