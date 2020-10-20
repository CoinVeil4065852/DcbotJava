package com.coin.discordBot.events.commands;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Command {
    public final String NAME;
    public final String DESCRIPTION;
    public final boolean CAN_CALL_IN_GUILD;
    public final boolean CAN_CALL_IN_PRIVATE;

    private Event event;

    public Command(String name, String description, boolean can_call_in_guild, boolean can_call_in_private) {
        this.NAME = name;
        this.DESCRIPTION = description;
        CAN_CALL_IN_GUILD = can_call_in_guild;
        CAN_CALL_IN_PRIVATE = can_call_in_private;
    }

    public void execute(MessageReceivedEvent event, String[] args) {
    }
}
