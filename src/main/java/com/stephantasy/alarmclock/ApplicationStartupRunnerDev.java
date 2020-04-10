package com.stephantasy.alarmclock;

import com.stephantasy.alarmclock.components.alarms.AlarmRepository;
import com.stephantasy.alarmclock.components.light.LightRepository;
import com.stephantasy.alarmclock.components.music.MusicRepository;
import com.stephantasy.alarmclock.core.Recurrence;
import com.stephantasy.alarmclock.core.RecurrenceType;
import com.stephantasy.alarmclock.core.exceptions.CustomHttpException;
import com.stephantasy.alarmclock.core.models.Alarm;
import com.stephantasy.alarmclock.core.models.Light;
import com.stephantasy.alarmclock.core.models.Music;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Profile("!prod")
public class ApplicationStartupRunnerDev implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStartupRunnerDev.class);

    private AlarmRepository alarmRepository;
    private MusicRepository musicRepository;
    private LightRepository lightRepository;

    @Autowired
    public ApplicationStartupRunnerDev(AlarmRepository alarmRepository, MusicRepository musicRepository, LightRepository lightRepository) {
        this.alarmRepository = alarmRepository;
        this.musicRepository = musicRepository;
        this.lightRepository = lightRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        AddDefault();
        AddAlarms();
    }

    private void AddDefault() {
        // Add default data
        Optional<Light> optLight = lightRepository.findById(1L);
        if (!optLight.isPresent()) {
            lightRepository.save(new Light());
        }
        Optional<Music> optMusic = musicRepository.findById(1L);
        if (!optMusic.isPresent()) {
            musicRepository.save(new Music());
        }
    }

    private void AddAlarms() {
        try {
            // Add data for tests
            for (Alarm a : List.of(
//                    new Alarm("alarm 001", LocalDateTime.now().minusMinutes(1).withSecond(0), "Desc", new Recurrence(RecurrenceType.Once, new boolean[]{false, false, false, false, false, false, false}), 1, 1, false, true),
                    new Alarm("alarm alarm 002", LocalDateTime.now().plusMinutes(1).withSecond(0), "Desc", new Recurrence(RecurrenceType.EveryDay, new boolean[]{true, true, true, true, true, true, true}), 1L, 1L, false, true),
                    new Alarm("alarm alarm 003", LocalDateTime.now().plusMinutes(2).withSecond(0), "Desc", new Recurrence(RecurrenceType.EveryDay, new boolean[]{true, true, true, true, true, true, true}), 1L, 1L, false, true)//,
//                    new Alarm("alarm 005", LocalDateTime.now().plusMinutes(3).withSecond(0), "Desc", new Recurrence(RecurrenceType.Once, new boolean[]{false, false, false, false, false, false, false}), 1, 1, false, true)
            )) {
                alarmRepository.save(a);
            }
        } catch (Exception e) {
            throw new CustomHttpException("Unable to fill DB with initial data!", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
