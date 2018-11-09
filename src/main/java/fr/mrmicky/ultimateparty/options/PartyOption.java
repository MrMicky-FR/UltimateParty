package fr.mrmicky.ultimateparty.options;

import fr.mrmicky.ultimateparty.locale.Message;

public enum PartyOption {

    RECEIVE_INVITATIONS(true, Message.OPTION_PARTY_INVITATION),
    PUBLIC_PARTY(false, Message.OPTION_PUBLIC_PARTY);

    private boolean defaultValue;
    private Message msg;

    PartyOption(boolean defaultValue, Message msg) {
        this.defaultValue = defaultValue;
        this.msg = msg;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public Message getMessage() {
        return msg;
    }
}