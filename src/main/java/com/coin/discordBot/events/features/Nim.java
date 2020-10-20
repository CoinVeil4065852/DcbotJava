package com.coin.discordBot.events.features;

import com.coin.discordBot.NimGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;

public class Nim extends ListenerAdapter {
    public static HashMap<Member,NimGame> userGames = new HashMap<>();

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(userGames.containsKey(event.getMember())){
            NimGame game=userGames.get(event.getMember());
            if(event.getMessageIdLong()==game.tempMessage.getIdLong()) {
                try {
                    game.UserTurn(Integer.parseInt(event.getReaction().getReactionEmote().getAsReactionCode().replace("\uFE0F\u20E3", "")));
                    event.getChannel().deleteMessageById(game.tempMessage.getIdLong()).queue();
                    if(game.CheckLose()){
                        event.getChannel().sendMessage(new EmbedBuilder().setTitle(event.getMember().getNickname()).setDescription("YOU LOSE").setColor(Color.RED).build()).queue();
                        userGames.remove(event.getMember());
                        return;
                    }

                    StringBuilder temp = new StringBuilder();
                    for (int i = 0; i < userGames.get(event.getMember()).GetNimCount(); i++) {
                        temp.append("\uD83D\uDFE5");
                        if(i%5==4){
                            temp.append("\n");
                        }
                    }
                    int tempCount =game.GetNimCount();
                    game.ComputerTurn();
                    if(game.CheckLose()){
                        event.getChannel().sendMessage(new EmbedBuilder().setTitle(event.getMember().getNickname()).setDescription("YOU WIN").setColor(Color.GREEN).build()).queue();
                        userGames.remove(event.getMember());
                        return;
                    }
                    StringBuilder temp2 = new StringBuilder();
                    int j = 0;
                    for (int i = 0; i < userGames.get(event.getMember()).GetNimCount(); i++) {
                        temp2.append("\uD83D\uDFE5");
                        if(i%5==4){
                            temp2.append("\n");
                        }
                        j=i+1;
                    }
                    for (int i = j; i < tempCount-game.GetNimCount()+j; i++) {
                        temp2.append("\uD83D\uDD33");
                        System.out.println(temp2.length());
                        if(i%5==4){
                            temp2.append("\n");
                        }
                    }
                    userGames.get(event.getMember()).tempMessage= event.getChannel().sendMessage(
                            new EmbedBuilder().setTitle(event.getMember().getNickname()).addField("Your turn : "+event.getReaction().getReactionEmote().getAsReactionCode().replace("\uFE0F\u20E3", ""), temp.toString(),false).addField("Computer's turn : "+Integer.toString(tempCount-game.GetNimCount()), temp2.toString(),false).build()).complete();
                    for (int i = 1; i <= 5; i++) {
                        userGames.get(event.getMember()).tempMessage.addReaction(i+"\uFE0F\u20E3").queue();
                    }

                } catch (Exception e) {
                }
            }
        }
    }
}
