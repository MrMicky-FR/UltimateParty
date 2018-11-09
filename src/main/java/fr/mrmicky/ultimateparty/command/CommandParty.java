package fr.mrmicky.ultimateparty.command;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.command.subcommands.*;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.utils.MessageBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandParty extends Command implements TabExecutor {

    private UltimateParty m;
    private List<PartyCommand> commands = new ArrayList<>();

    public CommandParty(String name, boolean permission, String[] aliases, UltimateParty m) {
        super(name, permission ? "ultimateparty.use" : null, aliases);
        this.m = m;
        loadCommands();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("ultimateparty.reload")) {
            m.reloadConfig();
            sender.sendMessage(new ComponentBuilder("Config reloaded").color(ChatColor.GREEN).create());
            return;
        }

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Message.NO_CONSOLE.getAsComponenent());
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;

        if (!m.isServerEnable(p)) {
            p.sendMessage(Message.DISABLE_SERVER_SELF.getAsComponenent());
            return;
        }

        if (args.length == 0) {
            openMenu(p);
        } else {
            PartyCommand cmd = getCommand(args[0]);
            if (cmd != null) {
                cmd.execute(p, Arrays.copyOfRange(args, 1, args.length), m.getPartyManager().getParty(p));
            } else {
                p.sendMessage(Message.UNKNOW_SUBCOMMAND.getAsComponenent());
            }
        }
    }

    private PartyCommand getCommand(String cmd) {
        for (PartyCommand c : commands) {
            if (c.getName().equalsIgnoreCase(cmd)) {
                return c;
            }
        }
        return null;
    }

    private void loadCommands() {
        register(new PartyAccept());
        register(new PartyChat());
        register(new PartyCreate());
        register(new PartyDebug());
        register(new PartyDeny());
        register(new PartyDisband());
        register(new PartyInvite());
        register(new PartyJoin());
        register(new PartyKick());
        register(new PartyLead());
        register(new PartyLeave());
        register(new PartyList());
        register(new PartyOptions());
        register(new PartyTp());
    }

    public void register(PartyCommand cmd) {
        commands.add(cmd);
    }

    public void openMenu(ProxiedPlayer p) {
        Party party = m.getPartyManager().getParty(p);

        p.sendMessage(Message.SPACER_TOP.getAsComponenent());

        if (party == null) {
            p.sendMessage(new MessageBuilder(Message.MENU_NO_PARTY.getMessage())
                    .click(Message.CREATE_BUTTON.getMessage(), true, m.getCommand() + " create",
                            Message.CREATE_BUTTON_HOVER.getMessage())
                    .click(Message.OPTIONS_BUTTON.getMessage(), true, m.getCommand() + " options",
                            Message.OPTIONS_BUTTON_HOVER.getMessage())
                    .build());
        } else {
            p.sendMessage(Message.PARTY_FORMAT_LEADER.getAsComponenent(party.getLeader().getName()));

            if (party.isLeader(p)) {
                party.getPlayers().forEach(ps -> {
                    if (ps != party.getLeader()) {
                        p.sendMessage(new MessageBuilder(Message.PARTY_FORMAT_WHEN_LEADER.getMessage(ps.getName()))
                                .click(Message.KICK_BUTTON.getMessage(), false,
                                        m.getCommand() + " kick " + ps.getName(),
                                        Message.KICK_BUTTON_HOVER.getMessage(ps.getName()))
                                .click(Message.LEAD_BUTTON.getMessage(), false,
                                        m.getCommand() + " lead " + ps.getName(),
                                        Message.LEAD_BUTTON_HOVER.getMessage(ps.getName()))
                                .build());
                    }
                });

                p.sendMessage(new TextComponent(" "));
                p.sendMessage(new MessageBuilder(Message.MENU_PARTY_LEADER.getMessage())
                        .click(Message.INVITE_BUTTON.getMessage(), false, m.getCommand() + " invite ",
                                Message.INVITE_BUTTON_HOVER.getMessage())
                        .click(Message.CHAT_BUTTON.getMessage(), false, m.getCommand() + " chat ",
                                Message.CHAT_BUTTON_HOVER.getMessage())
                        .click(Message.DISBAND_BUTTON.getMessage(), false, m.getCommand() + " disband",
                                Message.DISBAND_BUTTON_HOVER.getMessage())
                        .click(Message.OPTIONS_BUTTON.getMessage(), true, m.getCommand() + " options",
                                Message.OPTIONS_BUTTON_HOVER.getMessage())
                        .build());
            } else {
                party.getPlayers().forEach(ps -> {
                    if (ps != party.getLeader()) {
                        p.sendMessage(Message.PARTY_FORMAT.getAsComponenent(ps.getName()));
                    }
                });
                p.sendMessage(new TextComponent(""));
                p.sendMessage(new MessageBuilder(Message.MENU_PARTY_MEMBER.getMessage())
                        .click(Message.JOIN_BUTTON.getMessage(), true, m.getCommand() + " tp",
                                Message.JOIN_BUTTON_HOVER.getMessage())
                        .click(Message.CHAT_BUTTON.getMessage(), false, m.getCommand() + " chat ",
                                Message.CHAT_BUTTON_HOVER.getMessage())
                        .click(Message.LEAVE_BUTTON.getMessage(), false, m.getCommand() + " leave",
                                Message.LEAVE_BUTTON_HOVER.getMessage())
                        .click(Message.OPTIONS_BUTTON.getMessage(), true, m.getCommand() + " options",
                                Message.OPTIONS_BUTTON_HOVER.getMessage())
                        .build());
            }
        }

        p.sendMessage(Message.SPACER_BOTTOM.getAsComponenent());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (args.length == 1) {
                List<String> matches = new ArrayList<>();
                commands.forEach(cmd -> {
                    if (cmd.getName().startsWith(args[0].toLowerCase())) {
                        matches.add(cmd.getName());
                    }
                });
                return matches;
            } else if (args.length > 1) {
                PartyCommand cmd = getCommand(args[0]);
                if (cmd != null) {
                    ProxiedPlayer p = (ProxiedPlayer) sender;
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                    List<String> tab = cmd.onTabComplete(p, newArgs, m.getPartyManager().getParty(p));

                    if (tab != null) {
                        return tab;
                    }
                }
            }
        }
        return Collections.emptyList();
    }
}
