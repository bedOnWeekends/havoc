package com.bedonweekends.listener;

import com.bedonweekends.service.VoiceRecordService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashSet;

@Slf4j
public class VoiceRecord extends ListenerAdapter {

    public HashSet<String> voiceChannel = new HashSet<>();

    public VoiceRecordService voiceRecordService = new VoiceRecordService();

    @Override
    public void onGuildVoiceUpdate(@NonNull GuildVoiceUpdateEvent event) {
        if (event.getChannelJoined() != null && event.getChannelLeft() == null) {
            if (!voiceChannel.contains(event.getChannelJoined().getId())) {
                voiceChannel.add(event.getChannelJoined().getId());
                log.info("User {} joined voice channel {}", event.getEntity().getUser().getAsTag(), event.getChannelJoined().getName());
                voiceRecordService.startRecording(event.getGuild(), event.getChannelJoined().getId());
            }
        }else if (event.getChannelJoined() == null && event.getChannelLeft() != null){
            if (voiceChannel.contains(event.getChannelLeft().getId()) && event.getChannelLeft().getMembers().size() == 1 && event.getChannelLeft().getMembers().get(0).getUser().isBot()) {
                voiceChannel.remove(event.getChannelLeft().getId());
                log.info("Voice channel {} is empty", event.getChannelLeft().getName());
                try {
                    voiceRecordService.stopRecording(event.getGuild());
                } catch (Exception e) {
                    log.error("Error while stopping recording", e);
                }
            }
        }
    }
}
