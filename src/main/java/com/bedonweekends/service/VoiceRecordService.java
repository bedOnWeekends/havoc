package com.bedonweekends.service;

import com.bedonweekends.handler.AudioHandler;
import net.dv8tion.jda.api.entities.Guild;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VoiceRecordService {

    public AudioHandler audioHandler;

    public void startRecording(Guild guild, String voiceChannelId) {
        audioHandler = new AudioHandler();
        guild.getAudioManager().setReceivingHandler(audioHandler);
        guild.getAudioManager().openAudioConnection(guild.getVoiceChannelById(voiceChannelId));
    }

    public void stopRecording(Guild guild) throws IOException {
        guild.getAudioManager().closeAudioConnection();
        AudioInputStream ais = audioHandler.getAudioInputStream();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String now = LocalDateTime.now().format(formatter);
        File outputFile = new File("output_" + now + ".wav");
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outputFile);
        audioHandler = null;
    }
}
