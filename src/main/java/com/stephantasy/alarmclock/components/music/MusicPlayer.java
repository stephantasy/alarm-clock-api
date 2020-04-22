package com.stephantasy.alarmclock.components.music;

import com.stephantasy.alarmclock.core.exceptions.CustomHttpException;
import org.springframework.http.HttpStatus;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import static javax.sound.sampled.Clip.LOOP_CONTINUOUSLY;

public class MusicPlayer implements Runnable {

    private int id;
    private MusicCallback callback;
    private AudioInputStream audioIn = null;
    private Clip clip = null;
    private InputStream file;
    private VolumeManager volumeManager;
    private long volumeDuration;
    private boolean loop;
    private boolean DEBUG;

    public MusicPlayer(InputStream file, long volumeDuration, boolean loop, boolean debug) {
        this.file = file;
        this.callback = null;
        this.volumeDuration = volumeDuration;
        this.loop = loop;
        this.DEBUG = debug;
    }

    public MusicPlayer(int id, InputStream file, MusicCallback musicIsDone, long volumeDuration, boolean loop, boolean debug) {
        this.id = id;
        this.file = file;
        this.callback = musicIsDone;
        this.volumeDuration = volumeDuration;
        this.loop = loop;
        this.DEBUG = debug;
    }


    public void start() {
        if (clip != null) {
            if(loop) {
                // The loop has to be placed here, otherwise it wont loop anymore after a Pause (even if the clip was already defined as LOOP_CONTINUOUSLY before. Bug?)
                clip.loop(LOOP_CONTINUOUSLY);
            }
            clip.start();
        }
    }

    public void pause() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
        if (audioIn != null) {
            try {
                audioIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (file != null) {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isStillRunning(){
        return clip.isRunning();
    }

    @Override
    public void run() {
        try {
            BufferedInputStream bIN = new BufferedInputStream(file);
            audioIn = AudioSystem.getAudioInputStream(bIN);
            clip = AudioSystem.getClip();

            // Listener which allow method return once sound is completed
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    // Specific action if asked
                    if (callback != null) {
                        callback.musicIsFinished(this);
                    }
                    // Stop the Volume Manager (if it is not finished)
                    if (volumeManager != null) {
                        volumeManager.stopIt();
                    }
                    stop();
                }
            });
            clip.open(audioIn);

            // Set Volume to minimum
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Volume Control
            if (volumeDuration > 0) {
                control.setValue(control.getMinimum());
                volumeManager = new VolumeManager(clip, volumeDuration, DEBUG);
                new Thread(volumeManager).start();
            }else{
                control.setValue(control.getMaximum());
            }

            start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
            e1.printStackTrace();
            throw new CustomHttpException("Unable to play music!", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
