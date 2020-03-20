package com.stephantasy.alarmclock.components.music;

import com.stephantasy.alarmclock.core.events.AlarmEvent;
import com.stephantasy.alarmclock.core.exceptions.CustomHttpException;
import com.stephantasy.alarmclock.core.models.Music;
import com.stephantasy.alarmclock.core.services.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class MusicManager implements MusicService, ApplicationListener<AlarmEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MusicManager.class);

    @Value("${alarmclock.music-folder}")
    private String musicFolder;
    @Value("${alarmclock.default-music}")
    private String defaultMusic;

    private final static String MUSIC_PLAYING = "Music is Playing";
    private final static String MUSIC_PAUSED = "Music is Paused";
    private final static String MUSIC_STOPPED = "Music is Stopped";
    private static final String MUSIC_NONE = "No music is playing";
    @Autowired
    ResourceLoader resourceLoader;
    private boolean isPaused = false;
    private Runnable t;
    private long volumeDuration = 5; // in second

    @Override
    public List<Music> getMusics() {
        return null;
    }

    @Override
    public String play(InputStream file) {
        if (t != null) {
            if (isPaused) {
                ((MusicPlayer) t).start();
                isPaused = false;
                LOG.info(MUSIC_PLAYING);
                return MUSIC_PLAYING;
            } else {
                stop();
            }
        }

        t = new MusicPlayer(file, volumeDuration);
        new Thread(t).start();
        return MUSIC_PLAYING;
    }

    @Override
    public String pause() {
        if (t != null) {
            isPaused = true;
            ((MusicPlayer) t).pause();
            LOG.info(MUSIC_PAUSED);
            return MUSIC_PAUSED;
        } else {
            return MUSIC_NONE;
        }

    }

    @Override
    public String stop() {
        if (t != null) {
            ((MusicPlayer) t).stop();
            t = null;
            LOG.info(MUSIC_STOPPED);
            return MUSIC_STOPPED;
        } else {
            return MUSIC_NONE;
        }
    }

    @Override
    public void onApplicationEvent(AlarmEvent alarmEvent) {

        // Play Music
        {
            LOG.info("      => The music " + alarmEvent.getAlarm().getMusic().getName() + " is played");

            this.volumeDuration = alarmEvent.getAlarm().getMusic().getDelayBeforeFullSound();

            try {
                InputStream song = resourceLoader.getResource("classpath:" + musicFolder + defaultMusic).getInputStream();
                play(song);
            } catch (IOException e) {
                throw new CustomHttpException("Music " + defaultMusic + " not found!", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
    }
}

