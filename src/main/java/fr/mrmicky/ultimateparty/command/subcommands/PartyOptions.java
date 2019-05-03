package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.options.PartyOption;
import fr.mrmicky.ultimateparty.utils.MessageBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PartyOptions extends PartyCommand {

    public PartyOptions() {
        super("options");
    }

    @Override
    public void execute(ProxiedPlayer p, String[] args, Party party) {
        if (args == null || args.length < 2) {
            p.sendMessage(Message.SPACER_TOP.getAsComponenent());
            for (PartyOption option : PartyOption.values()) {
                boolean b = getPlugin().getDataManager().getOption(p, option);
                ChatColor color = b ? ChatColor.GREEN : ChatColor.RED;
                p.sendMessage(new MessageBuilder(
                        Message.OPTION_FORMAT.getMessage(color + option.getMessage().getMessage()))
                        .click(b ? Message.OPTION_DISABLE_BUTTON.getMessage() : Message.OPTION_ENABLE_BUTTON.getMessage(), true,
                                getPlugin().getCommand() + " options " + option.toString() + " " + !b, b ? Message.OPTION_DISABLE_BUTTON_HOVER.getMessage() : Message.OPTION_ENABLE_BUTTON_HOVER.getMessage())
                        .build());
            }

            p.sendMessage(Message.SPACER_BOTTOM.getAsComponenent());
        } else {
            PartyOption option;
            boolean b = Boolean.parseBoolean(args[1]);
            try {
                option = PartyOption.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                option = null;
            }

            if (option != null) {
                getPlugin().getDataManager().setOption(p, option, b);
                getPlugin().getDataManager().saveData();
            }

            execute(p, null, party);
        }
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
