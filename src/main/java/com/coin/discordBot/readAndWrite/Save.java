package com.coin.discordBot.readAndWrite;

import com.coin.discordBot.Main;
import com.google.gson.Gson;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Save {
    public static JsonFile cache = new JsonFile(new File("src/main/resources/Data/cache.json"));
    public static JsonFile config = new JsonFile(new File("src/main/resources/Data/config.json"));




    private static Gson gson = new Gson();

    public static void readFromJson(JsonFile file) {
        try {
            file.clear();
            file.putAll(gson.fromJson(new BufferedReader(new FileReader(file.file)),HashMap.class));
            System.out.println("read "+gson.fromJson(new BufferedReader(new FileReader(file.file)),HashMap.class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void readAll() {
        readFromJson(config);
        readFromJson(cache);

    }

    public static void writeToJson(JsonFile file) {
        try {
            FileWriter writer = new FileWriter(file.file);
            writer.write(gson.toJson(file));
            writer.close();
            System.out.println("write "+gson.toJson(file)+" from "+file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeAll(){
        writeToJson(config);
        writeToJson(cache);

    }

    public static Map<String, String> UCTCtoID(Map<User, TextChannel> hashMap) {
        HashMap<String, String> uCID = new HashMap<>();
        for (var u : hashMap.keySet()) {
            uCID.put(u.getId(), hashMap.get(u).getId());
        }
        return uCID;
    }

    public static Map<User, TextChannel> IDtoUCTC(Map<String, String> hashMap) {
        HashMap<User, TextChannel> uCID = new HashMap<>();
        for (var u : hashMap.keySet()) {
            uCID.put(Main.jda.getUserById(u), Main.jda.getTextChannelById(hashMap.get(u)));
        }
        return uCID;
    }



}


