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
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class PartyMainCommand extends Command implements TabExecutor {

    private final Map<String, PartyCommand> commands = new HashMap<>();
    private final UltimateParty plugin;

    public PartyMainCommand(String name, boolean permission, String[] aliases, UltimateParty plugin) {
        super(name, permission ? "ultimateparty.use" : null, aliases);

        this.plugin = plugin;

        register(new PartyAccept(plugin));
        register(new PartyChat(plugin));
        register(new PartyCreate(plugin));
        register(new PartyDebug(plugin));
        register(new PartyDeny(plugin));
        register(new PartyDisband(plugin));
        register(new PartyInvite(plugin));
        register(new PartyJoin(plugin));
        register(new PartyKick(plugin));
        register(new PartyLead(plugin));
        register(new PartyLeave(plugin));
        register(new PartyList(plugin));
        register(new PartyOptions(plugin));
        register(new PartyTp(plugin));
        register(new PartyWarp(plugin));
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

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (!plugin.isServerEnable(player)) {
            Message.DISABLE_SERVER_SELF.send(player);
            return;
        }

        if (args.length == 0) {
            openMenu(player);
            return;
        }

        PartyCommand cmd = commands.get(args[0].toLowerCase(Locale.ROOT));
        if (cmd == null) {
            Message.UNKNOWN_SUBCOMMAND.send(player);
            return;
        }

        try {
            cmd.execute(player, Arrays.copyOfRange(args, 1, args.length), plugin.getPartyManager().getParty(player));
        } catch (PartySilentCommandException e) {
            // ignore
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer) || args.length == 0) {
            return Collections.emptyList();
        }

        String rawCommand = args[0].toLowerCase(Locale.ROOT);

        if (args.length == 1) {
            return commands.keySet().stream()
                    .filter(cmd -> cmd.startsWith(rawCommand))
                    .collect(Collectors.toList());
        }

        PartyCommand command = commands.get(rawCommand);

        if (command != null) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            return command.onTabComplete(player, Arrays.copyOfRange(args, 1, args.length), plugin.getPartyManager().getParty(player));
        }

        return Collections.emptyList();
    }

    public void register(PartyCommand cmd) {
        commands.put(cmd.getName(), cmd);
    }

    public void openMenu(ProxiedPlayer player) {
        Party party = plugin.getPartyManager().getParty(player);

        Message.SPACER_TOP.send(player);

        if (party == null) {
            player.sendMessage(new MessageBuilder(Message.MENU_NO_PARTY)
                    .click(Message.CREATE_BUTTON, true, plugin.getCommand() + " create", Message.CREATE_BUTTON_HOVER)
                    .click(Message.OPTIONS_BUTTON, true, plugin.getCommand() + " options", Message.OPTIONS_BUTTON_HOVER)
                    .build());

            Message.SPACER_BOTTOM.send(player);

            return;
        }

        Message.PARTY_FORMAT_LEADER.send(player, party.getLeader().getName());

        if (party.isLeader(player)) {
            for (ProxiedPlayer ps : party.getPlayers()) {
                if (ps.equals(party.getLeader())) {
                    continue;
                }

                player.sendMessage(new MessageBuilder(Message.PARTY_FORMAT_WHEN_LEADER.getAsString(ps.getName()))
                        .click(Message.KICK_BUTTON.getAsString(), false, plugin.getCommand() + " kick " + ps.getName(),
                                Message.KICK_BUTTON_HOVER.getAsString(ps.getName()))
                        .click(Message.LEAD_BUTTON.getAsString(), false, plugin.getCommand() + " lead " + ps.getName(),
                                Message.LEAD_BUTTON_HOVER.getAsString(ps.getName()))
                        .build());
            }

            player.sendMessage(ChatUtils.SPACE_COMPONENT);
            player.sendMessage(new MessageBuilder(Message.MENU_PARTY_LEADER)
                    .click(Message.INVITE_BUTTON, false, plugin.getCommand() + " invite ", Message.INVITE_BUTTON_HOVER)
                    .click(Message.CHAT_BUTTON, false, plugin.getCommand() + " chat ", Message.CHAT_BUTTON_HOVER)
                    .click(Message.DISBAND_BUTTON, false, plugin.getCommand() + " disband", Message.DISBAND_BUTTON_HOVER)
                    .click(Message.OPTIONS_BUTTON, true, plugin.getCommand() + " options", Message.OPTIONS_BUTTON_HOVER)
                    .build());
        } else {
            for (ProxiedPlayer ps : party.getPlayers()) {
                if (ps != party.getLeader()) {
                    Message.PARTY_FORMAT.send(player, ps.getName());
                }
            }
            player.sendMessage(ChatUtils.SPACE_COMPONENT);
            player.sendMessage(new MessageBuilder(Message.MENU_PARTY_MEMBER)
                    .click(Message.JOIN_BUTTON, true, plugin.getCommand() + " tp", Message.JOIN_BUTTON_HOVER)
                    .click(Message.CHAT_BUTTON, false, plugin.getCommand() + " chat ", Message.CHAT_BUTTON_HOVER)
                    .click(Message.LEAVE_BUTTON, false, plugin.getCommand() + " leave", Message.LEAVE_BUTTON_HOVER)
                    .click(Message.OPTIONS_BUTTON, true, plugin.getCommand() + " options", Message.OPTIONS_BUTTON_HOVER)
                    .build());
        }

        Message.SPACER_BOTTOM.send(player);
    }
}
