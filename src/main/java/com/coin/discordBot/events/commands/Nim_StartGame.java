package com.coin.discordBot.events.commands;

import com.coin.discordBot.NimGame;
import com.coin.discordBot.events.features.Nim;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Nim_StartGame extends Command{
    public Nim_StartGame() {
        super("nim","let the bot play Nim with you",true, true);
    }
    
    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        if(Nim.userGames.containsKey(event.getMember()))return;
        Nim.userGames.put(event.getMember(),new NimGame(true));
        String temp = "";
        for (int i = 0; i < Nim.userGames.get(event.getMember()).GetNimCount(); i++) {
            temp=temp+ "\uD83D\uDFE5";
            if(i%5==4){
                temp=temp+"\n";
            }

        }
        Nim.userGames.get(event.getMember()).tempMessage= event.getChannel().sendMessage(new EmbedBuilder().setTitle(event.getMember().getNickname()).setDescription(temp).build()).complete();
        for (int i = 1; i <= 5; i++) {
            Nim.userGames.get(event.getMember()).tempMessage.addReaction(i+"\uFE0F\u20E3").queue();
        }
    }

   
}
