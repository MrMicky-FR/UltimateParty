package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.options.PartyOption;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import fr.mrmicky.ultimateparty.utils.MessageBuilder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.stream.Collectors;

public class PartyInvite extends PartyCommand {

    public PartyInvite() {
        super("invite");
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        if (party == null) {
            if (getPlugin().getConfig().getBoolean("AutoParty")) {
                party = getPartyManager().createParty(player);
                Message.PARTY_CREATE.send(player);
            } else {
                Message.NO_LEADER.send(player);
                return;
            }
        }

        if (!party.isLeader(player)) {
            Message.NO_LEADER.send(player);
            return;
        }

        if (args.length == 0) {
            Message.NO_PLAYER.send(player);
            return;
        }

        ProxiedPlayer p2 = getPlugin().getProxy().getPlayer(args[0]);

        if (p2 == null) {
            Message.PLAYER_NOT_FOUND.send(player);
            return;
        }

        if (getPartyManager().hasParty(p2)) {
            Message.ALREADY_IN_PARTY.send(player);
            return;
        }

        if (party.isFull()) {
            Message.PARTY_FULL_SELF.send(player);
            return;
        }

        if (!getPlugin().isServerEnable(p2)) {
            Message.DISABLE_SERVER.send(player);
            return;
        }

        if (party.isInvited(p2)) {
            Message.INVITATION_ALREADY_SEND.send(player);
            return;
        }

        if (!getPlugin().getDataManager().getOption(p2, PartyOption.RECEIVE_INVITATIONS)) {
            Message.DISABLE_INVITATION.send(player);
            return;
        }

        sendInvitation(p2, party);

        Message.INVITATION_SEND.send(player, p2.getName());
    }

    private void sendInvitation(ProxiedPlayer p, Party party) {
        String name = party.getLeader().getName();
        party.createInvitation(p);
        BaseComponent[] msg = new MessageBuilder(Message.INVITATION_RECEIVE.getAsString(name, getPartyManager().getInvitationDelay()))
                .click(Message.INVITATION_ACCEPT_BUTTON.getAsString(), true, getPlugin().getCommand() + " accept " + name, Message.INVITATION_ACCEPT_BUTTON_HOVER.getAsString())
                .click(Message.INVITATION_DENY_BUTTON.getAsString(), true, getPlugin().getCommand() + " deny " + name, Message.INVITATION_DENY_BUTTON_HOVER.getAsString())
                .build();
        p.sendMessage(msg);
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer player, String[] args, Party party) {
        if (args.length != 1) {
            return null;
        }

        return player.getServer().getInfo().getPlayers().stream()
                .filter(p -> !p.equals(player) && ChatUtils.startsWithIgnoreCase(p.getName(), args[0]))
                .map(CommandSender::getName)
                .collect(Collectors.toList());
    }
}
