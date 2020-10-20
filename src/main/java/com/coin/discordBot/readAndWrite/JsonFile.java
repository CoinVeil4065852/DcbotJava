package com.coin.discordBot.readAndWrite;

import java.io.File;
import java.util.HashMap;

public class JsonFile extends HashMap {
    public final File file ;
    JsonFile(File file) {
        this.file = file;
    }
    public Object getOrCreate(String key,Object o){
        if (!containsKey(key)) {
            put(key, o);
            System.out.println("create "+key);
        }
        return get(key);
    }
}