package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.utils.ChatCensor;
import fr.mrmicky.ultimateparty.utils.MessageBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartyChat extends PartyCommand {

    private static final DateFormat LOG_FORMAT = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss] ");

    public PartyChat() {
        super("chat");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        if (party != null) {
            sendMessage(p, party, String.join(" ", args), m);
        } else {
            p.sendMessage(Message.NO_PARTY.getAsComponenent());
        }
    }

    public static void sendMessage(ProxiedPlayer p, Party party, String msg, UltimateParty m) {
        ChatCensor cc = new ChatCensor(p, msg);

        if (cc.isCancel()) {
            return;
        }

        BaseComponent[] c = new MessageBuilder(Message.getPrefix() + "{0-}")
                .click(Message.CHAT_FORMAT.getMessage(m.getDisplayName(p)) + cc.getNewMessage(), false,
                        m.getCommand() + " chat ", Message.CHAT_BUTTON_HOVER.getMessage())
                .build();

        party.getPlayers().stream().filter(m::isServerEnable).forEach(ps -> ps.sendMessage(c));

        if (m.getConfig().getBoolean("Chat.Log")) {
            log("(" + party.getLeader().getName() + "'s party" + ") " + p.getName() + ": " + msg, m);
        }
    }

    private static void log(String message, UltimateParty m) {
        try {
            File f = new File(m.getDataFolder(), "logs.txt");

            if (!f.exists()) {
                f.createNewFile();
            }

            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
                pw.println(LOG_FORMAT.format(new Date()) + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer p, String[] args, Party party) {
        List<String> members = new ArrayList<>();
        for (ProxiedPlayer ps : party.getPlayers()) {
            if (ps != p && ps.getName().toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                members.add(ps.getName());
            }
        }
        return members;
    }
}
