package com.coin.discordBot.events;


import com.coin.discordBot.events.commands.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class Events extends ListenerAdapter {
    public static final String PREFIX = "-";
    private HashSet<Command> commands= new HashSet<>();
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        commands.add(new Nim_StartGame());
        commands.add(new Resend_SetChannel());
        commands.add(new Resend_Mention());
        commands.add(new Ping());
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        boolean isGuild = event.isFromGuild();
        if(event.getMessage().getContentRaw().toLowerCase().startsWith(PREFIX)) {
            String msg[] = event.getMessage().getContentRaw().replaceFirst(PREFIX,"").toLowerCase().split("\\s");
            for (Command command: commands) {
                if(msg[0].equals(command.NAME) &&((isGuild&&command.CAN_CALL_IN_GUILD)||(!isGuild&&command.CAN_CALL_IN_PRIVATE))){
                    command.execute(event,msg);
                }
            }

        }
    }
}
