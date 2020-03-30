package com.stephantasy.alarmclock;

import com.stephantasy.alarmclock.components.light.LightRepository;
import com.stephantasy.alarmclock.components.music.MusicRepository;
import com.stephantasy.alarmclock.core.exceptions.CustomHttpException;
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

import java.util.Optional;

@Component
@Profile("prod")
public class ApplicationStartupRunnerProd implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStartupRunnerProd.class);

    private MusicRepository musicRepository;
    private LightRepository lightRepository;

    @Autowired
    public ApplicationStartupRunnerProd(MusicRepository musicRepository, LightRepository lightRepository) {
        this.musicRepository = musicRepository;
        this.lightRepository = lightRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            // Add default data
            Optional<Light> optLight = lightRepository.findById(1L);
            if (!optLight.isPresent()) {
                lightRepository.save(new Light());
            }
            Optional<Music> optMusic = musicRepository.findById(1L);
            if (!optMusic.isPresent()) {
                musicRepository.save(new Music());
            }
        } catch (Exception e) {
            throw new CustomHttpException("Unable to fill DB with initial data!", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
