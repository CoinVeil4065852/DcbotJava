package com.coin.discordBot;

import com.coin.discordBot.events.Events;
import com.coin.discordBot.events.features.Arithmetic;
import com.coin.discordBot.events.features.Nim;
import com.coin.discordBot.events.features.Resend;
import com.coin.discordBot.readAndWrite.Save;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static JDA jda;
    public static void main (String[] agrs) throws Exception {
        Save.readAll();

/*        JPanel jPanel = new JPanel();
        JFrame jFrame = new JFrame();

        jFrame.setSize(800,600);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.add(jPanel);
        jFrame.setTitle("Coin's DCBot");

        jPanel.setLayout(null);

        JLabel jLabel = new JLabel();
        jLabel.setBounds(10,20,80,25);
        jPanel.add(jLabel);*/


        jda = new JDABuilder(AccountType.BOT).setToken("NzQxOTE1NjM0OTA4MjY2NTI2.Xy-gxw.4cPdIA8OZyGaeh-6Cnr9ODkzJdI").setActivity(Activity.playing("Cooking Lo Mei......")).build();
        jda.addEventListener(new Events());
        jda.addEventListener(new Arithmetic());
        jda.addEventListener(new Resend());
        jda.addEventListener(new Nim());

    }
    public static String unicodeToUtf8 (String s) throws UnsupportedEncodingException {
        return new String( s.getBytes(StandardCharsets.UTF_8) , StandardCharsets.UTF_8);
    }
    public static String Utf8toGBK (String s) throws UnsupportedEncodingException {
        return new String( s.getBytes("GBK") , "GBK");
    }
}
