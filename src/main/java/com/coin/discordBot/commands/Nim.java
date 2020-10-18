package com.coin.discordBot.commands;

import com.coin.discordBot.Main;
import com.coin.discordBot.NimGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;

public class Nim extends Command{
    public Nim() {
        super("nim","let the bot play Nim with you",true, true);
    }
    private HashMap<Member,NimGame> userGames = new HashMap<>();
    @Override
    public void execute(MessageReceivedEvent event) {
        if(userGames.containsKey(event.getMember()))return;
        userGames.put(event.getMember(),new NimGame(true));
        String temp = "";
        for (int i = 0; i < userGames.get(event.getMember()).GetNimCount(); i++) {
            temp=temp+ "\uD83D\uDFE5";
            if(i%5==4){
                temp=temp+"\n";
            }

        }
        userGames.get(event.getMember()).tempMessage= event.getChannel().sendMessage(new EmbedBuilder().setTitle(event.getMember().getNickname()).setDescription(temp).build()).complete();
        for (int i = 1; i <= 5; i++) {
            userGames.get(event.getMember()).tempMessage.addReaction(i+"\uFE0F\u20E3").queue();
        }
    }
}
