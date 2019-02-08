package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.PartyManager;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.options.PartyOption;
import fr.mrmicky.ultimateparty.utils.MessageBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PartyInvite extends PartyCommand {

    public PartyInvite() {
        super("invite");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        if (party == null) {
            if (m.getConfig().getBoolean("AutoParty")) {
                party = getPartyManager().createParty(p);
                p.sendMessage(Message.PARTY_CREATE.getAsComponenent());
            } else {
                p.sendMessage(Message.NO_LEADER.getAsComponenent());
                return;
            }
        }

        if (party.isLeader(p)) {
            if (args.length == 0) {
                p.sendMessage(Message.NO_PLAYER.getAsComponenent());
                return;
            }

            ProxiedPlayer p2 = m.getProxy().getPlayer(args[0]);

            if (p2 != null) {
                if (!getPartyManager().hasParty(p2)) {
                    if (!party.isFull()) {
                        if (!m.isServerEnable(p2)) {
                            p.sendMessage(Message.DISABLE_SERVER.getAsComponenent());
                            return;
                        }

                        if (party.isInvited(p2)) {
                            p.sendMessage(Message.INVITATION_ALREADY_SEND.getAsComponenent());
                            return;
                        }

                        if (!m.getDataManager().getOption(p2, PartyOption.RECEIVE_INVITATIONS)) {
                            p.sendMessage(Message.DISABLE_INVITATION.getAsComponenent());
                            return;
                        }

                        sendInvitation(p2, party);
                        p.sendMessage(Message.INVITATION_SEND.getAsComponenent(p2.getName()));
                    } else {
                        p.sendMessage(Message.PARTY_FULL_SELF.getAsComponenent());
                    }
                } else {
                    p.sendMessage(Message.ALREADY_IN_PARTY.getAsComponenent());
                }
            } else {
                p.sendMessage(Message.PLAYER_NOT_FOUND.getAsComponenent());
            }
        } else {
            p.sendMessage(Message.NO_LEADER.getAsComponenent());
        }
    }

    private void sendInvitation(ProxiedPlayer p, Party party) {
        String name = party.getLeader().getName();
        party.invite(p);
        BaseComponent[] msg = new MessageBuilder(Message.INVITATION_RECEIVE.getMessage(name, getPartyManager().getInvitationDelay()))
                .click(Message.INVITATION_ACCEPT_BUTTON.getMessage(), true, m.getCommand() + " accept " + name,
                        Message.INVITATION_ACCEPT_BUTTON_HOVER.getMessage())
                .click(Message.INVITATION_DENY_BUTTON.getMessage(), true, m.getCommand() + " deny " + name,
                        Message.INVITATION_DENY_BUTTON_HOVER.getMessage())
                .build();
        p.sendMessage(msg);
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
        if (args.length != 1) {
            return null;
        }

        List<String> members = new ArrayList<>();
        for (ProxiedPlayer ps : p.getServer().getInfo().getPlayers()) {
            if (ps != p && ps.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                members.add(ps.getName());
            }
        }
        return members;
    }
}
