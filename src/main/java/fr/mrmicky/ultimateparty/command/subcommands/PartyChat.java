package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.utils.ChatCensor;
import fr.mrmicky.ultimateparty.utils.ChatUtils;
import fr.mrmicky.ultimateparty.utils.MessageBuilder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PartyChat extends PartyCommand {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("[yyyy/MM/dd HH:mm:ss]");

    public PartyChat() {
        super("chat");
    }

    public static void sendMessage(ProxiedPlayer player, Party party, String msg, UltimateParty plugin) {
        ChatCensor censor = new ChatCensor(player, msg);

        if (censor.isCancel()) {
            return;
        }

        String rawMessage = Message.CHAT_FORMAT.getAsString(plugin.getDisplayName(player)) + censor.getNewMessage();

        BaseComponent[] c = new MessageBuilder(Message.PREFIX.getMessage() + "{0-}")
                .click(rawMessage, false, plugin.getCommand() + " chat ", Message.CHAT_BUTTON_HOVER.getAsString())
                .build();

        party.getPlayers().stream().filter(plugin::isServerEnable).forEach(ps -> ps.sendMessage(c));

        if (plugin.getConfig().getBoolean("Chat.Log")) {
            log('(' + party.getLeader().getName() + "'s party) " + player.getName() + ": " + msg, plugin);
        }
    }

    private static void log(String message, UltimateParty plugin) {
        try {
            File logFile = new File(plugin.getDataFolder(), "logs.txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(DATE_FORMATTER.format(LocalDateTime.now()) + ' ' + message);
                writer.newLine();
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save the log file", e);
        }
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        if (party == null) {
            Message.NO_PARTY.send(player);
            return;
        }

        sendMessage(player, party, String.join(" ", args), getPlugin());
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer player, String[] args, Party party) {
        return party.getPlayers().stream()
                .filter(p -> p != player && ChatUtils.startsWithIgnoreCase(p.getName(), args[args.length - 1]))
                .map(CommandSender::getName)
                .collect(Collectors.toList());
    }
}
