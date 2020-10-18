package com.coin.discordBot.commands;

import com.coin.discordBot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Ping extends Command{
    public Ping() {
        super("ping","return the bot's ping (ms)",true, true);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage(new EmbedBuilder().setTitle("Ping").setDescription(Long.toString(Main.jda.getGatewayPing())).build()).queue();
    }
}
