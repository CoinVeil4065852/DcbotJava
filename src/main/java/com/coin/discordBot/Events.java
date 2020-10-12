package com.coin.discordBot;



import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Events extends ListenerAdapter {
    private String prefix= "-";
    private boolean w= true;
    private HashMap<User,List<TextChannel>> userChannelHashMap=new HashMap<>();
    private HashMap<User,TextChannel> userCurrentTC =new HashMap<>();
    private HashMap<User,List<User>> userMentions = new HashMap<>();
    File file = new File("C:\\Users\\ryant\\Desktop\\untitled\\src\\main\\resources\\Temp\\UsercurrentTC");
 /*   public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        if (event.getMember().getUser().getIdLong() == 741915634908266526l) {
            if (w) {
                event.getMember().modifyNickname(event.getOldNickname()).queue();
                w = false;
            } else {
                w = true;
            }
        }
    }*/

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        userCurrentTC = IDtoUCTC((HashMap<Long, Long>) readObjectFromFile(file));
    }

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(event.getAuthor()==Main.jda.getSelfUser())return;
        for(User user:userCurrentTC.keySet()){
            if(userCurrentTC.get(user)==event.getChannel()) {
                user.openPrivateChannel().queue(channel ->
                        channel.sendMessage(new EmbedBuilder().setTitle(event.getMember().getNickname()).setDescription(event.getMessage().getContentRaw()).build()).queue());
            }
        }
    }

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {


        if(event.getAuthor()==Main.jda.getSelfUser()) return;
        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            String[] msg = event.getMessage().getContentRaw().replaceFirst(prefix, "").split("\\s");

            switch (msg[0].toLowerCase()){
                case "c"://set channel
                    if (msg.length == 1 || !userChannelHashMap.containsKey(event.getAuthor())) {
                        HashMap<Guild, List<TextChannel>> allChannels = new HashMap<>();
                        List<Guild> guilds = event.getAuthor().getMutualGuilds();
                        for (Guild g : guilds) {
                            List<GuildChannel> channels = g.getChannels();
                            List<TextChannel> channelInGuild = new ArrayList<>();
                            for (GuildChannel gc : channels) {
                                if (gc.getType() == ChannelType.TEXT) {
                                    channelInGuild.add(Main.jda.getTextChannelById(gc.getIdLong()));
                                }
                                allChannels.put(g, channelInGuild);
                            }
                        }
                        EmbedBuilder embed = new EmbedBuilder().setColor(Color.orange);
                        int i = 0;
                        List<TextChannel> usersChannels = new ArrayList<>();
                        for (Guild guild : allChannels.keySet()) {
                            StringBuilder temp = new StringBuilder();
                            for (TextChannel tc : allChannels.get(guild)) {
                                i++;
                                temp.append("(").append(i).append(")").append(tc.getName()).append("\n");
                                usersChannels.add(tc);
                            }
                            embed.addField(guild.getName(), temp.toString(), false);
                        }
                        userChannelHashMap.put(event.getAuthor(), usersChannels);


                        event.getChannel().sendMessage(embed.setDescription("Type : " + prefix + "c [channel index]").build()).queue();

                    }else {
                        try {
                            int i = Integer.parseInt(msg[1]);
                            userCurrentTC.put(event.getAuthor(), userChannelHashMap.get(event.getAuthor()).get(i - 1));
                            SaveUserCurrentCTC();
                            //event.getMessage().addReaction("").queue();
                            event.getChannel().sendMessage(new EmbedBuilder().setTitle("Channel has set to").setColor(Color.GREEN).addField(userChannelHashMap.get(event.getAuthor()).get(i - 1).getGuild().getName(), userChannelHashMap.get(event.getAuthor()).get(i - 1).getName(), false).build()).queue();
                        } catch (Exception e) {
                            event.getChannel().sendMessage(e.toString()).queue();
                        }
                    }
                    break;
                case "m"://mention
                    //user has a channel
                    if(userCurrentTC.containsKey(event.getAuthor())) {
                        if (msg.length == 1 || !userMentions.containsKey(event.getAuthor())) {
                            List<Member> guildMembers = userCurrentTC.get(event.getAuthor()).getGuild().getMembers();
                            EmbedBuilder embed = new EmbedBuilder().setColor(Color.orange);
                            int i = 0;
                            List<User> guildUsers = new ArrayList<>();
                            StringBuilder temp = new StringBuilder();
                            for (Member member : guildMembers) {
                                i++;
                                temp.append("(").append(i).append(")").append(member.getUser().getName()).append("\n");
                                guildUsers.add(member.getUser());

                            }
                            userMentions.put(event.getAuthor(),guildUsers);
                            embed.addField(userCurrentTC.get(event.getAuthor()).getGuild().getName(), temp.toString(), false);

                            event.getChannel().sendMessage(embed.setDescription("Type : " + prefix + "m [user index]").build()).queue();

                        }
                        if (msg.length > 1 && userMentions.containsKey(event.getAuthor())) {
                            //try {
                                int i = Integer.parseInt(msg[1]);
                                userCurrentTC.get(event.getAuthor()).sendMessage(userMentions.get(event.getAuthor()).get(i-1).getAsMention()).queue();
                            //} catch (Exception e) {
                              //  event.getChannel().sendMessage(e.toString()).queue();
                            //}
                        }
                    }else {
                        event.getChannel().sendMessage(new EmbedBuilder().setTitle("You haven't set a channel yet").setDescription("Type : " + prefix + "c").build()).queue();
                    }
                    break;
            }
        } else {
            if (userCurrentTC.containsKey(event.getAuthor())) {
                userCurrentTC.get(event.getAuthor()).sendMessage(event.getMessage()).queue();
            } else {
                event.getChannel().sendMessage(new EmbedBuilder().setTitle("You haven't set a channel yet").setDescription("Type : " + prefix + "c").build()).queue();
            }
        }
        System.out.println(UCTCtoID(userCurrentTC));
        System.out.println(IDtoUCTC(UCTCtoID(userCurrentTC)));


    }
    public void SaveUserCurrentCTC(){
        writeObjectToFile(UCTCtoID(userCurrentTC),file);
    }

    //
/*    public static HashMap<Long,List<Long>> UCtoID(HashMap<User,List<TextChannel>> hashMap){
        HashMap<Long,List<Long>> uCID = new HashMap<>();
        for (var u:hashMap.keySet()){
            var tcList =hashMap.get(u);
            List<Long> tcListId = new ArrayList<>();
            for (var tc:tcList){
                tcListId.add(tc.getIdLong());
            }
            uCID.put(u.getIdLong(),tcListId);
        }
        return uCID;
    }
    public static HashMap<User,List<TextChannel>> IDtoUC (HashMap<Long,List<Long>> hashMap){
        HashMap<User,List<TextChannel>> uCID = new HashMap<>();
        for (var u:hashMap.keySet()){
            var tcList =hashMap.get(u);
            List<TextChannel> tcListId = new ArrayList<>();
            for (var tc:tcList){
                tcListId.add(Main.jda.getTextChannelById(tc));
            }
            uCID.put(Main.jda.getUserById(u),tcListId);
        }
        return uCID;
    }*/
    //
    public static HashMap<Long,Long> UCTCtoID(HashMap<User,TextChannel> hashMap){
        HashMap<Long,Long> uCID = new HashMap<>();
        for (var u:hashMap.keySet()){
            uCID.put(u.getIdLong(),hashMap.get(u).getIdLong());
        }
        return uCID;
    }
    public static HashMap<User,TextChannel> IDtoUCTC (HashMap<Long,Long> hashMap){
        HashMap<User,TextChannel> uCID = new HashMap<>();
        for (var u:hashMap.keySet()){
            uCID.put(Main.jda.getUserById(u),Main.jda.getTextChannelById(hashMap.get(u)));
        }
        return uCID;
    }
//


    public static void writeObjectToFile(Object obj,File file)
    {
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }
    public static Object readObjectFromFile(File file)
    {
        Object temp=null;
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp=objIn.readObject();
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
