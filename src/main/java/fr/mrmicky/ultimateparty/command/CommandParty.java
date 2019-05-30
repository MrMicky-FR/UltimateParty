package fr.mrmicky.ultimateparty.command;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.command.subcommands.*;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import fr.mrmicky.ultimateparty.utils.MessageBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandParty extends Command implements TabExecutor {

    private final Map<String, PartyCommand> commands = new HashMap<>();
    private final UltimateParty plugin;

    public CommandParty(String name, boolean permission, String[] aliases, UltimateParty plugin) {
        super(name, permission ? "ultimateparty.use" : null, aliases);

        this.plugin = plugin;

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

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("ultimateparty.reload")) {
            plugin.loadConfig();
            sender.sendMessage(ChatUtils.newComponent("Config reloaded", ChatColor.GREEN));
            return;
        }

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatUtils.newComponent("Only players can use party commands", ChatColor.RED));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;

        if (!plugin.isServerEnable(p)) {
            Message.DISABLE_SERVER_SELF.send(p);
            return;
        }

        if (args.length == 0) {
            openMenu(p);
            return;
        }

        PartyCommand cmd = commands.get(args[0].toLowerCase());
        if (cmd == null) {
            Message.UNKNOW_SUBCOMMAND.send(p);
            return;
        }

        try {
            cmd.execute(p, Arrays.copyOfRange(args, 1, args.length), plugin.getPartyManager().getParty(p));
        } catch (PartySilentCommandException e) {
            // ignore
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer) || args.length == 0) {
            return Collections.emptyList();
        }

        String rawCommand = args[0].toLowerCase();

        if (args.length == 1) {
            return commands.keySet().stream()
                    .filter(cmd -> cmd.startsWith(rawCommand))
                    .collect(Collectors.toList());
        }

        PartyCommand command = commands.get(rawCommand);

        if (command != null) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            List<String> tab = command.onTabComplete(p, Arrays.copyOfRange(args, 1, args.length), plugin.getPartyManager().getParty(p));

            if (tab != null) {
                return tab;
            }
        }

        return Collections.emptyList();
    }

    public void register(PartyCommand cmd) {
        commands.put(cmd.getName(), cmd);
    }

    public void openMenu(ProxiedPlayer p) {
        Party party = plugin.getPartyManager().getParty(p);

        Message.SPACER_TOP.send(p);

        if (party == null) {
            p.sendMessage(new MessageBuilder(Message.MENU_NO_PARTY.getAsString())
                    .click(Message.CREATE_BUTTON.getAsString(), true, plugin.getCommand() + " create",
                            Message.CREATE_BUTTON_HOVER.getAsString())
                    .click(Message.OPTIONS_BUTTON.getAsString(), true, plugin.getCommand() + " options",
                            Message.OPTIONS_BUTTON_HOVER.getAsString())
                    .build());

            Message.SPACER_BOTTOM.send(p);

            return;
        }

        Message.PARTY_FORMAT_LEADER.send(p, party.getLeader().getName());

        if (party.isLeader(p)) {
            for (ProxiedPlayer ps : party.getPlayers()) {
                if (ps.equals(party.getLeader())) {
                    continue;
                }

                p.sendMessage(new MessageBuilder(Message.PARTY_FORMAT_WHEN_LEADER.getAsString(ps.getName()))
                        .click(Message.KICK_BUTTON.getAsString(), false, plugin.getCommand() + " kick " + ps.getName(),
                                Message.KICK_BUTTON_HOVER.getAsString(ps.getName()))
                        .click(Message.LEAD_BUTTON.getAsString(), false, plugin.getCommand() + " lead " + ps.getName(),
                                Message.LEAD_BUTTON_HOVER.getAsString(ps.getName()))
                        .build());
            }

            p.sendMessage(ChatUtils.SPACE_COMPONENT);
            p.sendMessage(new MessageBuilder(Message.MENU_PARTY_LEADER.getAsString())
                    .click(Message.INVITE_BUTTON.getAsString(), false, plugin.getCommand() + " invite ",
                            Message.INVITE_BUTTON_HOVER.getAsString())
                    .click(Message.CHAT_BUTTON.getAsString(), false, plugin.getCommand() + " chat ",
                            Message.CHAT_BUTTON_HOVER.getAsString())
                    .click(Message.DISBAND_BUTTON.getAsString(), false, plugin.getCommand() + " disband",
                            Message.DISBAND_BUTTON_HOVER.getAsString())
                    .click(Message.OPTIONS_BUTTON.getAsString(), true, plugin.getCommand() + " options",
                            Message.OPTIONS_BUTTON_HOVER.getAsString())
                    .build());
        } else {
            for (ProxiedPlayer ps : party.getPlayers()) {
                if (ps != party.getLeader()) {
                    Message.PARTY_FORMAT.send(p, ps.getName());
                }
            }
            p.sendMessage(ChatUtils.SPACE_COMPONENT);
            p.sendMessage(new MessageBuilder(Message.MENU_PARTY_MEMBER.getAsString())
                    .click(Message.JOIN_BUTTON.getAsString(), true, plugin.getCommand() + " tp",
                            Message.JOIN_BUTTON_HOVER.getAsString())
                    .click(Message.CHAT_BUTTON.getAsString(), false, plugin.getCommand() + " chat ",
                            Message.CHAT_BUTTON_HOVER.getAsString())
                    .click(Message.LEAVE_BUTTON.getAsString(), false, plugin.getCommand() + " leave",
                            Message.LEAVE_BUTTON_HOVER.getAsString())
                    .click(Message.OPTIONS_BUTTON.getAsString(), true, plugin.getCommand() + " options",
                            Message.OPTIONS_BUTTON_HOVER.getAsString())
                    .build());
        }

        Message.SPACER_BOTTOM.send(p);
    }
}
