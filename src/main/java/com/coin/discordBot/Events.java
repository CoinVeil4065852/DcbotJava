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
import java.util.*;
import java.util.List;

public class Events extends ListenerAdapter {
    private String prefix = "-";
    private boolean w = true;
    private HashMap<User, List<TextChannel>> userChannelHashMap = new HashMap<>();
    private HashMap<User, TextChannel> userCurrentTC = new HashMap<>();
    private HashMap<User, List<User>> userMentions = new HashMap<>();
    private HashMap<User, TextChannel> userLogChannel = new HashMap<>();
    File file = new File("C:\\Users\\ryant\\Desktop\\untitled\\src\\main\\resources\\Temp\\UsercurrentTC");
    private static final long logChannelID = 765520394949361674L;
    private static final long logGuildID = 740831483304083528L;
    private Guild logGuild;
    private TextChannel logChannel;
    private Set<String> keyWords = new HashSet<>();


    @Override
    public void onReady(@NotNull ReadyEvent event) {
        keyWords.add("昱升");
        keyWords.add("湯昱");
        keyWords.add("ryan");
        keyWords.add("老公");
        keyWords.add("老婆");
        keyWords.add("喜歡");

        keyWords.add("小升升");
        keyWords.add("親愛");
        keyWords.add("90611");
        keyWords.add("阿湯");


        Load();
        logChannel = Main.jda.getTextChannelById(logChannelID);
        logGuild = Main.jda.getGuildById(logGuildID);




    }

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor() == Main.jda.getSelfUser()) return;
        for (User user : userCurrentTC.keySet()) {
            if (userCurrentTC.get(user) == event.getChannel()) {
                user.openPrivateChannel().queue(channel ->
                        channel.sendMessage(new EmbedBuilder().setTitle(event.getMember().getNickname()).setDescription(event.getMessage().getContentRaw()).build()).queue());
            }
        }
        //Math
        try {
            String temp =Double.toString(Math(event.getMessage().getContentRaw()));
            event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setTitle(event.getMessage().getContentRaw()+"=").setDescription(temp).build()).queue();
        }catch (Exception e){ }
    }

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        System.out.println(Utf8ToGBK(event.getMessage().getContentRaw()));



        if (event.getAuthor() == Main.jda.getSelfUser()) return;
        LogToChannel(event.getAuthor(), event.getMessage());
        Save();


        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            String[] msg = event.getMessage().getContentRaw().replaceFirst(prefix, "").split("\\s");

            switch (msg[0].toLowerCase()) {
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

                    } else {
                        try {
                            int i = Integer.parseInt(msg[1]);
                            userCurrentTC.put(event.getAuthor(), userChannelHashMap.get(event.getAuthor()).get(i - 1));
                            Save();
                            //event.getMessage().addReaction("").queue();
                            event.getChannel().sendMessage(new EmbedBuilder().setTitle("Channel has set to").setColor(Color.GREEN).addField(userChannelHashMap.get(event.getAuthor()).get(i - 1).getGuild().getName(), userChannelHashMap.get(event.getAuthor()).get(i - 1).getName(), false).build()).queue();
                        } catch (Exception e) {
                            event.getChannel().sendMessage(e.toString()).queue();
                        }
                    }
                    break;
                case "m"://mention
                    //user has a channel
                    if (userCurrentTC.containsKey(event.getAuthor())) {
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
                            userMentions.put(event.getAuthor(), guildUsers);
                            embed.addField(userCurrentTC.get(event.getAuthor()).getGuild().getName(), temp.toString(), false);

                            event.getChannel().sendMessage(embed.setDescription("Type : " + prefix + "m [user index]").build()).queue();

                        }
                        if (msg.length > 1 && userMentions.containsKey(event.getAuthor())) {
                            //try {
                            int i = Integer.parseInt(msg[1]);
                            userCurrentTC.get(event.getAuthor()).sendMessage(userMentions.get(event.getAuthor()).get(i - 1).getAsMention()).queue();
                            //} catch (Exception e) {
                            //  event.getChannel().sendMessage(e.toString()).queue();
                            //}
                        }
                    } else {
                        event.getChannel().sendMessage(new EmbedBuilder().setTitle("You haven't set a channel yet").setDescription("Type : " + prefix + "c").build()).queue();
                    }
                    break;
            }
        } else {
            if (userCurrentTC.containsKey(event.getAuthor())) {
                if (Check(event.getMessage(), userCurrentTC.get(event.getAuthor()), 10))
                    userCurrentTC.get(event.getAuthor()).sendMessage(event.getMessage()).queue();


            } else {
                event.getChannel().sendMessage(new EmbedBuilder().setTitle("You haven't set a channel yet").setDescription("Type : " + prefix + "c").build()).queue();
            }
        }

    }

    public void Save() {
        HashMap<String, Object> save = new HashMap<>();
        save.put("userCurrentTC", UCTCtoID(userCurrentTC));
        save.put("userLogChannel", UCTCtoID(userLogChannel));

        writeObjectToFile(save, file);
    }

    public void Load() {
        HashMap<String, Object> save = (HashMap<String, Object>) readObjectFromFile(file);
        try {
            userCurrentTC = IDtoUCTC((HashMap<Long, Long>) save.get("userCurrentTC"));
            userLogChannel = IDtoUCTC((HashMap<Long, Long>) save.get("userLogChannel"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void LogToChannel(User user, Message message) {
        if (!userLogChannel.containsKey(user))
            userLogChannel.put(user, logGuild.createTextChannel(user.getName()).complete());
        userLogChannel.get(user).sendMessage(message).queue();
        logChannel.sendMessage(userLogChannel.get(user).getAsMention() + ":" + message.getContentRaw()).queue();
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
    public static HashMap<Long, Long> UCTCtoID(HashMap<User, TextChannel> hashMap) {
        HashMap<Long, Long> uCID = new HashMap<>();
        for (var u : hashMap.keySet()) {
            uCID.put(u.getIdLong(), hashMap.get(u).getIdLong());
        }
        return uCID;
    }

    public static HashMap<User, TextChannel> IDtoUCTC(HashMap<Long, Long> hashMap) {
        HashMap<User, TextChannel> uCID = new HashMap<>();
        for (var u : hashMap.keySet()) {
            uCID.put(Main.jda.getUserById(u), Main.jda.getTextChannelById(hashMap.get(u)));
        }
        return uCID;
    }
//


    public static void writeObjectToFile(Object obj, File file) {
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }

    public static Object readObjectFromFile(File file) {
        Object temp = null;
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = objIn.readObject();
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

    public boolean Check(Message message, TextChannel channel, int amount) {
        List<Message> messages = channel.getHistory().retrievePast(amount).complete();
        messages.add(0, message);
        Collections.reverse(messages);
        Set<Message> messageToDelete = new HashSet<>();
        Message deleteMessage=null;
        String usersendkeyword = "";
        boolean delete = false;
        for (String keyword : keyWords) {
            String allMessage = "";
            String[] keywordSplit = keyword.split("");
            int keywordIndex = 0;
            for (Message m : messages) {///
                String[] messageSplit = m.getContentRaw().split("");
                for (String s : messageSplit) {
                    if (keywordSplit[keywordIndex].equalsIgnoreCase(s)) {
                        allMessage = allMessage + s;
                        if (m.getAuthor() == Main.jda.getSelfUser()) {
                            messageToDelete.add(m);
                            deleteMessage=m;
                        }
                        keywordIndex++;
                        if (keywordIndex > keywordSplit.length - 1)
                            keywordIndex = 0;
                    }
                }
            }
            System.out.println(allMessage);
            if (allMessage.contains(keyword)) {
                delete = true;
                usersendkeyword = usersendkeyword + keyword + "\n";
            }
        }


        boolean canSend = !delete || messageToDelete.remove(message);
        System.out.println(canSend);
        if (delete) {
            System.out.println("delete");

            try {
                if(messageToDelete.size()>1){
                    channel.deleteMessages(messageToDelete).queue();
                }else {
                    channel.deleteMessageById(deleteMessage.getIdLong()).queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!canSend)
            message.getChannel().sendMessage(new EmbedBuilder().setTitle("請不要輸入以下關鍵字").setDescription(usersendkeyword).setColor(Color.RED).build()).queue();
        return canSend;
    }

    public static String GBKToUtf8(String s) {
        try {
            return new String(s.getBytes("GBK"), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String Utf8ToGBK(String s) {
        try {
            return new String(s.getBytes("utf-8"), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public double Math(String message) throws Exception {
        String messageArgs[]= message.split("");
        List<String> mathArgs = new ArrayList<>();
        String number="";
        for (String arg : messageArgs) {
            try{
                Double.parseDouble(arg);
                number=number+arg;
            }catch (Exception e){
                if(!number.isEmpty())mathArgs.add(number);
                number="";
                mathArgs.add(arg);
            }
        }
        if(!number.isEmpty())mathArgs.add(number);
        return (MathHandler(mathArgs));
    }
    public double MathHandler(List<String> mathArgs) throws Exception{
        double ans = 0;
        //( )
        for (int i = 0; i <mathArgs.size() ; i++) {
            String arg = mathArgs.get(i);
            if(arg.equals("(")){
                for (int j = mathArgs.size()-1; j >=0 ; j--) {

                    if(mathArgs.get(j).equals(")")) {

                        MathHandler(mathArgs.subList(i + 1, j));
                        mathArgs.remove(i);
                        mathArgs.remove(i+1);
                        break;

                    }
                }
            }
        }
        //* /
        for (int i = 1; i < mathArgs.size()-1; i++) {

            String arg = mathArgs.get(i);
            if(arg.equals("*")||arg.equals("/")){
                double a = Double.parseDouble(mathArgs.get(i-1));
                double b = Double.parseDouble(mathArgs.get(i+1));
                mathArgs.subList(i-1,i+2).clear();
                if (arg.equals("*"))
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
        return Double.parseDouble(mathArgs.get(0)) ;
    }
}
