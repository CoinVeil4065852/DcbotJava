package com.coin.discordBot.events.commands;

import com.coin.discordBot.events.Events;
import com.coin.discordBot.events.features.Resend;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resend_Mention extends Command {
    public Resend_Mention() {
        super("m","mention a member in current channel",false, true);
    }
    private Map<User, List<Member>> userMentions = new HashMap<>();
    @Override
    public void execute(MessageReceivedEvent event,String args[]) {
        if (Resend.userCurrentTC.containsKey(event.getAuthor())) {
            if (args.length == 1 || !userMentions.containsKey(event.getAuthor())) {
                List<Member> guildMembers = Resend.userCurrentTC.get(event.getAuthor()).getGuild().getMembers();
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.orange);

                StringBuilder temp = new StringBuilder();
                for (Member member : guildMembers) {
                    temp.append("(").append(guildMembers.indexOf(member)+1).append(")");
                    if (member.getNickname()!=null)
                        temp.append(member.getNickname());
                    else
                        temp.append(member.getEffectiveName());
                    temp.append("\n");

                }
                userMentions.put(event.getAuthor(), guildMembers);
                embed.addField(Resend.userCurrentTC.get(event.getAuthor()).getGuild().getName(), temp.toString(), false);
                embed.setDescription("Type : " + Events.PREFIX + "m [user index]");
                event.getChannel().sendMessage(embed.build()).queue();

            }
            if (args.length > 1 && userMentions.containsKey(event.getAuthor())) {
                try {
                    int i = Integer.parseInt(args[1]);
                    Resend.userCurrentTC.get(event.getAuthor()).sendMessage(userMentions.get(event.getAuthor()).get(i - 1).getAsMention()).queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage(e.toString()).queue();
                }
            }
        } else {
            event.getChannel().sendMessage(new EmbedBuilder().setTitle("You haven't set a channel yet").setDescription("Type : " + Events.PREFIX + "c").build()).queue();
        }
    }
    
}
