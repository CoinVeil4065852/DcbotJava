package com.coin.discordBot;

import com.coin.discordBot.Excepions.NumberOutOfRangeException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.math.MathContext;
import java.sql.ShardingKey;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

public class NimGame{
   /* private final Member member;
    private final TextChannel textChannel;*/

    private int nimCount;
    private int max;
    public Message tempMessage;
    boolean hard;

    public NimGame(boolean hard/*Member member,TextChannel textChannel*/){
       /* this.member =member;
        this.textChannel =textChannel;*/
        this.hard  =hard;
        nimCount=25;
        max = 5;
    }
    public NimGame(/*Member member,TextChannel textChannel*/){
       /* this.member =member;
        this.textChannel =textChannel;*/
        this.hard  =false;
        nimCount=25;
        max = 5;
    }
    public void UserTurn(int count) throws Exception {
        if (count<0||count>max) throw new NumberOutOfRangeException();
        nimCount-=count;
        System.out.println(nimCount);
        if (CheckLose()) {
            System.out.println("you lose");
            return;
        }
    }
    public void ComputerTurn(){
        int num = 0;
        for (int i = 1; i <=nimCount ; i+=max+1) {
            num = i;
        }
        if (hard||nimCount<max+1) {
            if(num==nimCount){
                nimCount-= new Random().nextInt(5) +1;
            }else if(num<nimCount){
                nimCount=num;
            }
        }else {
            nimCount-= new Random().nextInt(5) +1;
        }

        System.out.println(nimCount);
        if(CheckLose()) {
            System.out.println("you win");
            return;
        }
    }
    public boolean CheckLose(){
        return nimCount<=0;
    }
    public int GetNimCount(){
        return nimCount;
    }
}
