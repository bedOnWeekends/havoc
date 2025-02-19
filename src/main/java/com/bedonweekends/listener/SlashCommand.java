package com.bedonweekends.listener;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Slf4j
public class SlashCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        log.info("Slash command received: {}", event.getName());
        switch (event.getName()) {
            case "say" -> {
                String content = Objects.requireNonNull(event.getOption("content")).getAsString();
                event.replyEmbeds(format("말함", content)).queue();
            }
        }

    }

    private MessageEmbed format(String title, Object content){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        if (content instanceof String) {
            embedBuilder.setDescription((String) content);
        }
        return embedBuilder.build();
    }

}
