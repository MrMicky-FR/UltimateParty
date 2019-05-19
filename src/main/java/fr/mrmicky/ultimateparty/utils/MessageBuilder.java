package fr.mrmicky.ultimateparty.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {

    private final List<MessageContainer> messages = new ArrayList<>();

    private String message;

    public MessageBuilder(String message) {
        this.message = message;
    }

    public MessageBuilder click(String msg, boolean runCommand, String clickValue, String hoverValue) {
        ClickEvent.Action clickAction = runCommand ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND;

        messages.add(new MessageContainer(msg, clickAction, clickValue, hoverValue));
        return this;
    }

    public BaseComponent[] build() {
        ComponentBuilder builder = new ComponentBuilder("");

        for (int i = 0; i < messages.size(); i++) {

            char[] chars = message.toCharArray();

            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                if (c == '{' && chars[j + 2] == '-' && chars[j + 3] == '}') {
                    int k = Character.getNumericValue(chars[j + 1]);
                    String[] str = message.split("\\{" + k + "-}");
                    MessageContainer mc = messages.get(k);
                    builder.append("", FormatRetention.NONE).append(TextComponent.fromLegacyText(str[0]))
                            .append(mc.getMessage(), FormatRetention.NONE).event(mc.getClickEvent())
                            .event(mc.getHoverEvent());
                    message = message.substring(str[0].length() + 4);
                    break;
                }
            }
        }

        return builder.append("", FormatRetention.NONE).append(TextComponent.fromLegacyText(message)).create();
    }

    class MessageContainer {

        private final String message;
        private final ClickEvent clickEvent;
        private final HoverEvent hoverEvent;

        public MessageContainer(String message, ClickEvent.Action clickAction, String clickCommand, String hover) {
            this.message = message;

            clickEvent = new ClickEvent(clickAction, '/' + clickCommand);
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hover));
        }

        public String getMessage() {
            return message;
        }

        public ClickEvent getClickEvent() {
            return clickEvent;
        }

        public HoverEvent getHoverEvent() {
            return hoverEvent;
        }
    }
}
