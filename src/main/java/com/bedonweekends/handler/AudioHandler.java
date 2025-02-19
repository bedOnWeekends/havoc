package com.bedonweekends.handler;

import lombok.Getter;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.SequenceInputStream;

public class AudioHandler implements AudioReceiveHandler {
    @Getter
    private AudioInputStream audioInputStream;
    private final AudioFormat format = AudioReceiveHandler.OUTPUT_FORMAT;

    @Override
    public boolean canReceiveCombined() {
        return true;
    }

    @Override
    public void handleCombinedAudio(CombinedAudio combinedAudio) {
        byte[] data = combinedAudio.getAudioData(1.0);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        if (audioInputStream == null) {
            audioInputStream = new AudioInputStream(bais, format, data.length);
        } else {
            audioInputStream = new AudioInputStream(
                    new SequenceInputStream(audioInputStream, new AudioInputStream(bais, format, data.length)),
                    format, AudioSystem.NOT_SPECIFIED);
        }
    }

}

