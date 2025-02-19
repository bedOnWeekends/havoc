package com.bedonweekends;

import com.bedonweekends.listener.Message;
import com.bedonweekends.listener.Ready;
import com.bedonweekends.listener.SlashCommand;
import com.bedonweekends.listener.VoiceRecord;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        String token = System.getProperty("botToken");
        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new Ready())
                .addEventListeners(new Message())
                .addEventListeners(new SlashCommand())
                .addEventListeners(new VoiceRecord())
                .build()
                .awaitReady();

        Guild guild = jda.getGuilds().get(0);
        guild.upsertCommand("say", "말함")
                .addOption(OptionType.STRING, "content", "내용", true)
                .queue();


    }
}