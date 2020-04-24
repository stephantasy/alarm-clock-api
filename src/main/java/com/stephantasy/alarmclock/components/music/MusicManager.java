package com.stephantasy.alarmclock.components.music;

import com.stephantasy.alarmclock.core.Timer;
import com.stephantasy.alarmclock.core.events.AlarmEvent;
import com.stephantasy.alarmclock.core.exceptions.CustomHttpException;
import com.stephantasy.alarmclock.core.exceptions.FetchAlarmException;
import com.stephantasy.alarmclock.core.models.Music;
import com.stephantasy.alarmclock.core.services.MusicService;
import com.stephantasy.alarmclock.dto.MusicDto;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MusicManager implements MusicService, ApplicationListener<AlarmEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MusicManager.class);
    @Value("${alarmclock.debug}")
    private boolean DEBUG;

    @Value("${alarmclock.music-folder}")
    private String musicFolder;
    @Value("${alarmclock.sound-folder}")
    private String soundFolder;
    @Value("${alarmclock.short-sound}")
    private String shortSound;

    @Value("${alarmclock.max-duration}")
    private String maxDuration;

    private MusicRepository musicRepository;

    private final static String MUSIC_PLAYING = "Music is Playing";
    private final static String MUSIC_PAUSED = "Music is Paused";
    private final static String MUSIC_STOPPED = "Music is Stopped";
    private static final String MUSIC_NONE = "No music is playing";

    private static final int SOUND_INTERVAL = 10*60;   // In second
    private static final int TIME_BETWEEN_SOUNDS = 500;   // In millisecond
    private static final int MAX_PLAYED_SOUND = 10;

    @Autowired
    ResourceLoader resourceLoader;

    private boolean isPaused = false;
    private Runnable player;
    private Runnable timer;
    private Thread timerThread;
    private long volumeDuration = 5; // in second
    private Stream<Path> walk;

    // Short Time
    private int nbPlayed;
    private LocalDateTime nextShortSoundTime;

    @Autowired
    private MusicManager(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @Override
    public MusicDto getMusic(long id) {
        Optional<Music> music = musicRepository.findById(id);
        if (music.isPresent()) {
            return music.get().toDto();
        }
        throw new FetchAlarmException("Music not found with id: " + id);
    }

    @Override
    public List<MusicDto> getMusics() {
        return musicRepository.findAll()
                .stream()
                .map(Music::toDto)
                .collect(Collectors.toList());
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

            // Be sure that timer is stopped before
            if (timerThread != null) {
                try {
                    timerThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        player = new MusicPlayer(file, volumeDuration, true, DEBUG);
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
            // Get the Music in DB
            long id = alarmEvent.getAlarm().getMusicID();
            Optional<Music> optMusic = musicRepository.findById(id);
            if (!optMusic.isPresent()) {
                throw new FetchAlarmException("Light not found with id: " + id);
            }
            Music music = optMusic.get();
            if (DEBUG) LOG.info("      => The music " + music.getName() + " is played");

            try {
                // Set the volume duration
                this.volumeDuration = music.getDelayBeforeFullSound();

                // Reset
                destroyTimer();

                // Short Sound init
                nbPlayed = 0;
                nextShortSoundTime = LocalDateTime.now().plusSeconds(SOUND_INTERVAL);


                // Run Timer (to limit time of running)
                timer = new Timer("Music", Long.parseLong(maxDuration), this::stop, this::playShortSound, DEBUG);
                timerThread = new Thread(timer, "Music Timer");
                timerThread.start();


                // Play main Song
                InputStream song = getRandomSong();
                play(song);

            } catch (Exception e) {
                throw new CustomHttpException("Music not found!", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
    }

    // TODO: make it a thread?
    // This is a sound that is played every 10 minutes to remind time passed since the beginning
    private void playShortSound() {
        // if time has come
        if (LocalDateTime.now().isAfter(nextShortSoundTime)) {
            nbPlayed++;
            if(nbPlayed > MAX_PLAYED_SOUND) nbPlayed = MAX_PLAYED_SOUND;
            nextShortSoundTime = LocalDateTime.now().plusSeconds(SOUND_INTERVAL);
            for (int i = 0 ; i < nbPlayed ; i++) {
                Runnable shortSoundThreadTemp = new MusicPlayer(getRemindTimeSong(), 0, false, DEBUG);
                new Thread(shortSoundThreadTemp).start();
                try {
                    Thread.sleep(TIME_BETWEEN_SOUNDS);
                } catch (InterruptedException e) {
                    // Sleep Exception, don't care
                }
            }
        }
    }
    private InputStream getRemindTimeSong() {
        String musicToPlay = soundFolder + shortSound;
        try {
            return new FileInputStream(musicToPlay);
        } catch (FileNotFoundException e) {
            throw new CustomHttpException("File " + musicToPlay + " not found!", HttpStatus.INTERNAL_SERVER_ERROR.value());
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

    @Override
    public String postpone() {
        //TODO
        return null;
    }


    private void stopTimer() {
        if (timer != null) {
            ((Timer) timer).stopIt();
        }
    }

    // WARNING: A thread cannot kill itself by using a callback function call!
    private void destroyTimer() {
        if (timer != null) {
            ((Timer) timer).stopIt();
            try {
                timer = null;
                timerThread.join();
            } catch (InterruptedException e) {
                LOG.error(e.getMessage());
            }
        }
    }

}
