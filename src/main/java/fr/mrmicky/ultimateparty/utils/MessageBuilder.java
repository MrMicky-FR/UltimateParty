package fr.mrmicky.ultimateparty.utils;

import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {

    private final List<TextComponent> extraComponents = new ArrayList<>();

    private String message;

    public MessageBuilder(String message) {
        this.message = message;
    }

    public MessageBuilder(Message message) {
        this(message.getAsString());
    }

    public MessageBuilder click(TextComponent message, boolean runCommand, String clickValue, BaseComponent[] hoverValue) {
        ClickEvent.Action clickAction = runCommand ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND;

        ClickEvent clickEvent = new ClickEvent(clickAction, '/' + clickValue);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverValue);

        message.setClickEvent(clickEvent);
        message.setHoverEvent(hoverEvent);

        extraComponents.add(message);

        return this;
    }

    public MessageBuilder click(Message message, boolean runCommand, String clickValue, Message hoverValue) {
        return click(new TextComponent(message.getAsComponent()), runCommand, clickValue, hoverValue.getAsComponent());
    }

    public MessageBuilder click(String message, boolean runCommand, String clickValue, String hoverValue) {
        return click(new TextComponent(TextComponent.fromLegacyText(message)), runCommand, clickValue, TextComponent.fromLegacyText(hoverValue));
    }

    public TextComponent build() {
        TextComponent mainComponent = new TextComponent();

        for (int i = 0; i < extraComponents.size(); i++) {

            char[] chars = message.toCharArray();

            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                if (c == '{' && chars[j + 2] == '-' && chars[j + 3] == '}') {
                    int k = Character.getNumericValue(chars[j + 1]);
                    String[] str = message.split("\\{" + k + "-}");

                    ChatUtils.addLegacyExtra(mainComponent, str[0]).addExtra(extraComponents.get(k));

                    message = message.substring(str[0].length() + 4);
                    break;
                }
            }
        }

        return ChatUtils.addLegacyExtra(mainComponent, message);
    }
}
