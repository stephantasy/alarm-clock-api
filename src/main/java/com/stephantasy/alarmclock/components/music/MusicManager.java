package com.stephantasy.alarmclock.components.music;

import com.stephantasy.alarmclock.core.Timer;
import com.stephantasy.alarmclock.core.events.AlarmEvent;
import com.stephantasy.alarmclock.core.exceptions.CustomHttpException;
import com.stephantasy.alarmclock.core.models.Music;
import com.stephantasy.alarmclock.core.services.MusicService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MusicManager implements MusicService, ApplicationListener<AlarmEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MusicManager.class);
    @Value("${alarmclock.debug}")
    private boolean DEBUG;

    @Value("${alarmclock.music-folder}")
    private String musicFolder;

    private final static String MUSIC_PLAYING = "Music is Playing";
    private final static String MUSIC_PAUSED = "Music is Paused";
    private final static String MUSIC_STOPPED = "Music is Stopped";
    private static final String MUSIC_NONE = "No music is playing";

    @Autowired
    ResourceLoader resourceLoader;
    private boolean isPaused = false;
    private Runnable player;
    private Runnable timer;
    private long volumeDuration = 5; // in second
    private Stream<Path> walk;

    @Override
    public List<Music> getMusics() {
        return null;
    }

    @Override
    public String play(InputStream file) {
        if (player != null) {
            if (isPaused) {
                ((MusicPlayer) player).start();
                isPaused = false;
                if (DEBUG) LOG.info(MUSIC_PLAYING);
                return MUSIC_PLAYING;
            } else {
                stop();
            }
        }

        player = new MusicPlayer(file, volumeDuration, DEBUG);
        new Thread(player).start();
        return MUSIC_PLAYING;
    }

    @Override
    public String pause() {
        if (player != null) {
            isPaused = true;
            ((MusicPlayer) player).pause();
            if (DEBUG) LOG.info(MUSIC_PAUSED);
            return MUSIC_PAUSED;
        } else {
            return MUSIC_NONE;
        }
    }

    @Override
    public String stop() {
        String result;
        if (player != null) {
            ((MusicPlayer) player).stop();
            player = null;
            if (DEBUG) LOG.info(MUSIC_STOPPED);
            result = MUSIC_STOPPED;
        } else {
            result = MUSIC_NONE;
        }
        // Stop Timer
        stopTimer();
        return result;
    }

    @Override
    public void onApplicationEvent(@NotNull AlarmEvent alarmEvent) {
        // Play Music
        {
            if (DEBUG) LOG.info("      => The music " + alarmEvent.getAlarm().getMusic().getName() + " is played");

            try {
                // Set the volume duration
                this.volumeDuration = alarmEvent.getAlarm().getMusic().getDelayBeforeFullSound();

                // Get Music
                InputStream song = getRandomSong();

                play(song);

                // Run Timer (to limit time of running)
                stopTimer();
                timer = new Timer("Music", 3600, this::stop, DEBUG);
                new Thread(timer).start();

            } catch (Exception e) {
                throw new CustomHttpException("Music not found!", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
    }

    @Override
    public InputStream getRandomSong() {
        String musicToPlay = "Undefined";
        try {
            // List of files int he Music folder
            Stream<Path> walk = Files.walk(Paths.get(musicFolder));
            List<String> musicList = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
            // Choose a random file
            int fileNumber = (int) (Math.random() * musicList.size());
            musicToPlay = musicList.get(fileNumber);

            return new FileInputStream(musicToPlay);
        } catch (FileNotFoundException e) {
            throw new CustomHttpException("File " + musicToPlay + " not found!", HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (IOException e) {
            throw new CustomHttpException("Folder " + musicFolder + " not found!", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @Override
    public boolean getState() {
        return (player != null);
    }

    private void stopTimer() {
        if (timer != null) {
            ((Timer) timer).stopIt();
            timer = null;
        }
    }

}
