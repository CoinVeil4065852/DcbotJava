package com.coin.discordBot.events.commands;

import com.coin.discordBot.events.Events;
import com.coin.discordBot.Main;
import com.coin.discordBot.events.features.Resend;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resend_SetChannel extends Command {
    public Resend_SetChannel() {
        super("c","set the channel you want to resend",false, true);
    }

    public static Map<User, List<TextChannel>> userChannelHashMap = new HashMap<>();
    @Override
    public void execute(MessageReceivedEvent event,String args[]) {
        if (args.length == 1 || !userChannelHashMap.containsKey(event.getAuthor())) {
            Map<Guild, List<TextChannel>> allChannels = new HashMap<>();
            List<Guild> guilds = event.getAuthor().getMutualGuilds();
            for (Guild g : guilds) {
                List<TextChannel> channels = new ArrayList<>();
                g.getChannels().forEach(guildChannel -> {if(guildChannel.getType()==ChannelType.TEXT)channels.add(Main.jda.getTextChannelById(guildChannel.getId()));});
                allChannels.put(g, channels);
            }
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.orange);
            List<TextChannel> usersChannels = new ArrayList<>();
            for (Guild guild : allChannels.keySet()) {
                StringBuilder temp = new StringBuilder();
                for (TextChannel tc : allChannels.get(guild)) {
                    usersChannels.add(tc);
                    temp.append("(").append(usersChannels.size()).append(")").append(tc.getName()).append("\n");

                }
                embed.addField(guild.getName(), temp.toString(), false);
            }
            userChannelHashMap.put(event.getAuthor(), usersChannels);
            embed.setDescription("Type : " + Events.PREFIX + "c [channel index]");

            event.getChannel().sendMessage(embed.build()).queue();
        } else {
            try {
                int i = Integer.parseInt(args[1]);
                Resend.userCurrentTC.put(event.getAuthor(), userChannelHashMap.get(event.getAuthor()).get(i - 1));
                Resend.saveToId();
                event.getChannel().sendMessage(new EmbedBuilder().setTitle("Channel has set to").setColor(Color.GREEN).addField(userChannelHashMap.get(event.getAuthor()).get(i - 1).getGuild().getName(), userChannelHashMap.get(event.getAuthor()).get(i - 1).getName(), false).build()).queue();
            } catch (Exception e) {
                event.getChannel().sendMessage(e.toString()).queue();
            }
        }
    }
}
