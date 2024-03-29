package fr.mrmicky.ultimateparty.command.subcommands;

import fr.mrmicky.ultimateparty.Party;
import fr.mrmicky.ultimateparty.UltimateParty;
import fr.mrmicky.ultimateparty.command.PartyCommand;
import fr.mrmicky.ultimateparty.locale.Message;
import fr.mrmicky.ultimateparty.options.PartyOption;
import fr.mrmicky.ultimateparty.utils.MessageBuilder;
import fr.mrmicky.ultimateparty.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PartyOptions extends PartyCommand {

    public PartyOptions(UltimateParty plugin) {
        super("options", plugin);
    }

    @Override
    public void execute(ProxiedPlayer player, String[] args, Party party) {
        if (args == null || args.length < 2) {
            Message.SPACER_TOP.send(player);

            for (PartyOption option : PartyOption.values()) {
                boolean b = getPlugin().getDataManager().isOptionEnabled(player, option);
                ChatColor color = b ? ChatColor.GREEN : ChatColor.RED;
                String rawMessage = Message.OPTION_FORMAT.getAsString(color + option.getMessage().getAsString());
                Message hoverMessage = b ? Message.OPTION_DISABLE_BUTTON_HOVER : Message.OPTION_ENABLE_BUTTON_HOVER;

                player.sendMessage(new MessageBuilder(rawMessage)
                        .click(b ? Message.OPTION_DISABLE_BUTTON : Message.OPTION_ENABLE_BUTTON, true,
                                getPlugin().getCommand() + " options " + option + " " + !b, hoverMessage)
                        .build());
            }

            Message.SPACER_BOTTOM.send(player);
        } else {
            PartyOption option;
            boolean b = Boolean.parseBoolean(args[1]);
            try {
                option = PartyOption.valueOf(args[0].toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                option = null;
            }

            if (option != null) {
                getPlugin().getDataManager().setOption(player, option, b);
                getPlugin().getDataManager().saveData();
            }

            execute(player, null, party);
        }
    }

    @Override
    public List<String> onTabComplete(ProxiedPlayer player, String[] args, Party party) {
        if (args.length != 1) {
            return Collections.emptyList();
        }

        return player.getServer().getInfo().getPlayers().stream()
                .filter(p -> !p.equals(player) && StringUtils.startsWithIgnoreCase(p.getName(), args[0]))
                .map(CommandSender::getName)
                .collect(Collectors.toList());
    }
}
