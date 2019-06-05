package fr.mrmicky.ultimateparty.utils;

import fr.mrmicky.ultimateparty.locale.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageBuilder {

    private final List<BaseComponent> extraComponents = new ArrayList<>();

    private String message;

    public MessageBuilder(String message) {
        this.message = message;
    }

    public MessageBuilder(Message message) {
        this(message.getAsString());
    }

    private MessageBuilder click(BaseComponent[] components, boolean runCommand, String clickValue, BaseComponent[] hoverValue) {
        BaseComponent component = components.length == 1 ? components[0] : new TextComponent(components);

        ClickEvent.Action clickAction = runCommand ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND;
        ClickEvent clickEvent = new ClickEvent(clickAction, '/' + clickValue);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverValue);

        component.setClickEvent(clickEvent);
        component.setHoverEvent(hoverEvent);

        extraComponents.add(component);

        return this;
    }

    public MessageBuilder click(Message message, boolean runCommand, String clickValue, Message hoverValue) {
        return click(message.getAsComponent(), runCommand, clickValue, hoverValue.getAsComponent());
    }

    public MessageBuilder click(String message, boolean runCommand, String clickValue, String hoverValue) {
        return click(TextComponent.fromLegacyText(message), runCommand, clickValue, TextComponent.fromLegacyText(hoverValue));
    }

    public BaseComponent[] build() {
        List<BaseComponent> components = new ArrayList<>();

        for (int i = 0; i < extraComponents.size(); i++) {

            char[] chars = message.toCharArray();

            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == '{' && chars[j + 2] == '-' && chars[j + 3] == '}') {
                    int value = Character.getNumericValue(chars[j + 1]);
                    String[] str = message.split("\\{" + value + "-}");

                    Collections.addAll(components, TextComponent.fromLegacyText(str[0]));
                    components.add(extraComponents.get(value));

                    message = message.substring(str[0].length() + 4);
                    break;
                }
            }
        }

        Collections.addAll(components, TextComponent.fromLegacyText(message));

        return components.toArray(new BaseComponent[0]);
    }
}
