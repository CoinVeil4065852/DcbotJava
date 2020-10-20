package com.coin.discordBot.events.features;

import com.coin.discordBot.Main;
import com.coin.discordBot.events.Events;
import com.coin.discordBot.readAndWrite.Save;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Resend extends ListenerAdapter {
    public static Map<User, TextChannel> userCurrentTC = new HashMap<>();
    private static String  logChannelId;
    private static TextChannel logChannel;

    private static String logGuildId;
    private static Guild logGuild ;
    private static Map<User, TextChannel> userLogChannelMap =new HashMap<>();
    private static String botCategoryId;
    private static Category botCategory;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        load();
    }
    public static void load(){
        try { userLogChannelMap =Save.IDtoUCTC((Map<String, String>) Save.cache.getOrCreate("userLogChannelMap",new HashMap<String, String>()));}catch (Exception ignored){ignored.printStackTrace();}
        try { userCurrentTC = Save.IDtoUCTC((Map<String, String>) Save.cache.getOrCreate("userCurrentTC",new HashMap<String,String>()));}catch (Exception ignored){ignored.printStackTrace();}
        try { logChannelId = (String) Save.config.getOrCreate("logChannelId", "");}catch (Exception ignored){ignored.printStackTrace();}
        try { logChannel=Main.jda.getTextChannelById(logChannelId);}catch (Exception ignored){ignored.printStackTrace();}
        try { logGuildId = (String) Save.config.getOrCreate("logGuildId", "");}catch (Exception ignored){ignored.printStackTrace();}
        try { logGuild = Main.jda.getGuildById(logGuildId);}catch (Exception ignored){ignored.printStackTrace();}
        try { botCategoryId = (String) Save.cache.getOrCreate("botCategoryId", "");}catch (Exception ignored){ignored.printStackTrace();}
        try { botCategory=Main.jda.getCategoryById(botCategoryId);}catch (Exception ignored){ignored.printStackTrace();}
    }
    public static void saveToId(){
        try { Save.cache.put("userLogChannelMap",Save.UCTCtoID(userLogChannelMap));}catch (Exception e){e.printStackTrace();}
        try { Save.cache.put("userCurrentTC",Save.UCTCtoID(userCurrentTC)); }catch (Exception e){e.printStackTrace();}
        try { Save.config.put("logChannelId",logChannel.getId());}catch (Exception e){e.printStackTrace();}
        try {Save.config.put("logGuildId",logGuild.getId()); }catch (Exception e){e.printStackTrace();}
        try { Save.cache.put("botCategoryId",botCategory.getId()); }catch (Exception e){e.printStackTrace();}
        Save.writeAll();
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if(event.getAuthor()==Main.jda.getSelfUser()) return;

        LogToChannel(event.getAuthor(), event.getMessage());
        if (event.getMessage().getContentRaw().startsWith(Events.PREFIX)) return;
        if (userCurrentTC.containsKey(event.getAuthor())) {
            userCurrentTC.get(event.getAuthor()).sendMessage(event.getMessage()).queue();
        } else {
            event.getChannel().sendMessage(new EmbedBuilder().setTitle("You haven't set a channel yet").setDescription("Type : " + Events.PREFIX + "c").build()).queue();
        }
    }
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor() == Main.jda.getSelfUser()) return;
        for (User user : userCurrentTC.keySet()) {
            if (userCurrentTC.get(user) == event.getChannel()) {
                user.openPrivateChannel().queue(channel ->
                        channel.sendMessage(new EmbedBuilder().setTitle(Objects.requireNonNull(event.getMember()).getNickname()).setDescription(event.getMessage().getContentRaw()).build()).queue());
            }
        }
    }
    public void LogToChannel(User user, Message message) {
        System.out.println("logging");
        if (botCategory == null) {
            botCategory = logGuild.createCategory(Main.jda.getSelfUser().getName()).complete();
            saveToId();
        }
        if (!userLogChannelMap.containsKey(user)) {
            userLogChannelMap.put(user, botCategory.createTextChannel(user.getName()).complete());
            saveToId();
        }
        userLogChannelMap.get(user).sendMessage(message).queue();
        logChannel.sendMessage(userLogChannelMap.get(user).getAsMention() + ":" + message.getContentRaw()).queue();
    }
}
