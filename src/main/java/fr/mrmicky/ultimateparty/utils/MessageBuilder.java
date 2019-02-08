package fr.mrmicky.ultimateparty.utils;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {

    private String msg;
    private List<MessageContainer> messages = new ArrayList<>();

    public MessageBuilder(String msg) {
        this.msg = msg;
    }

    public MessageBuilder click(String msg, boolean runCommand, String clickValue, String hoverValue) {
        messages.add(new MessageContainer(msg,
                runCommand ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND, clickValue,
                hoverValue));
        return this;
    }

    public BaseComponent[] build() {
        ComponentBuilder builder = new ComponentBuilder("");

        for (int i = 0; i < messages.size(); i++) {

            char[] chars = msg.toCharArray();

            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                if (c == '{' && chars[j + 2] == '-' && chars[j + 3] == '}') {
                    int k = Character.getNumericValue(chars[j + 1]);
                    String[] str = msg.split("\\{" + k + "-\\}");
                    MessageContainer mc = messages.get(k);
                    builder.append("", FormatRetention.NONE).append(TextComponent.fromLegacyText(str[0]));
                    builder.append(mc.getMessage(), FormatRetention.NONE).event(mc.getClickEvent())
                            .event(mc.getHoverEvent());
                    msg = msg.substring(str[0].length() + 4);
                    break;
                }
            }
        }
        builder.append("", FormatRetention.NONE).append(TextComponent.fromLegacyText(msg));
        return builder.create();
    }

    class MessageContainer {

        private String message;
        private ClickEvent clickEvent;
        private HoverEvent hoverEvent;

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
