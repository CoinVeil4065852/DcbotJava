package com.coin.discordBot.events.features;

import com.coin.discordBot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Arithmetic extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor() == Main.jda.getSelfUser()) return;
        try {
            String message =event.getMessage().getContentRaw().replaceAll("\\s+","").replaceAll("=","");
            String temp =Double.toString(Math(message));
            event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setTitle(message+"=").setDescription(temp).build()).queue();
        }catch (Exception ignored){ }
    }
    public double Math(String message) throws Exception {
        String[] messageArgs = message.split("");
        List<String> mathArgs = new ArrayList<>();
        String number="";
        for (String arg : messageArgs) {
            try{
                Double.parseDouble(arg);
                number=number+arg;
            }catch (Exception e){
                if(arg.equals("."))number=number+arg;
                else {
                    if (!number.isEmpty()) mathArgs.add(number);
                    number = "";
                    mathArgs.add(arg);
                }
            }
        }
        if(!number.isEmpty())mathArgs.add(number);
        return (MathHandler(mathArgs));
    }
    public double MathHandler(List<String> mathArgs) throws Exception{
        if(mathArgs.size()<2) throw new Exception();
        double ans = 0;
        System.out.println(mathArgs);
        //( )
        for (int i = 0; i <mathArgs.size() ; i++) {
            String arg = mathArgs.get(i);
            if(arg.equals("(")){
                int bracketsCount = 0;
                for (int j =i+1; j <mathArgs.size() ; j++) {
                    if(mathArgs.get(j).equals("("))
                        bracketsCount++;
                    if(mathArgs.get(j).equals(")")) {
                        if(bracketsCount==0) {
                            MathHandler(mathArgs.subList(i + 1, j));
                            mathArgs.remove(i);
                            mathArgs.remove(i + 1);
                            break;
                        }
                        bracketsCount--;
                    }
                }
            }
        }
        // ^
        for (int i = 1; i < mathArgs.size()-1; i++) {

            String arg = mathArgs.get(i);
            if(arg.equals("^")){
                double a = Double.parseDouble(mathArgs.get(i-1));
                double b = Double.parseDouble(mathArgs.get(i+1));
                mathArgs.subList(i-1,i+2).clear();
                mathArgs.add(i-1,Double.toString(Math.pow(a,b)));
                i--;
                System.out.println(mathArgs);
            }
        }
        //* /
        for (int i = 1; i < mathArgs.size()-1; i++) {

            String arg = mathArgs.get(i);
            if(arg.equals("*")||arg.equals("x")||arg.equals("/")||arg.equals("÷")){
                double a = Double.parseDouble(mathArgs.get(i-1));
                double b = Double.parseDouble(mathArgs.get(i+1));
                mathArgs.subList(i-1,i+2).clear();
                if (arg.equals("*")||arg.equals("x"))
                    mathArgs.add(i-1,Double.toString(a*b));
                else
                    mathArgs.add(i-1,Double.toString(a/b));
                i--;

                System.out.println(mathArgs);
            }
        }
        for (int i = 1; i < mathArgs.size()-1; i++) {

            String arg = mathArgs.get(i);
            if(arg.equals("+")||arg.equals("-")){
                double a = Double.parseDouble(mathArgs.get(i-1));
                double b = Double.parseDouble(mathArgs.get(i+1));
                mathArgs.subList(i-1,i+2).clear();
                if (arg.equals("+"))
                    mathArgs.add(i-1,Double.toString(a+b));
                else
                    mathArgs.add(i-1,Double.toString(a-b));
                i--;

                System.out.println(mathArgs);
            }
        }
        System.out.println("finish"+mathArgs.get(0));
        if(mathArgs.size()!=1) throw new Exception();
        return Double.parseDouble(mathArgs.get(0));
    }
}
