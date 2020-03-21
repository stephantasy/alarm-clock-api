package com.stephantasy.alarmclock;

import com.stephantasy.alarmclock.components.alarms.AlarmRepository;
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

@Component
@Profile("!prod")
public class ApplicationStartupRunner implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStartupRunner.class);

    private AlarmRepository alarmRepository;

    @Autowired
    public ApplicationStartupRunner(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            // Add data for tests
            for (Alarm a : List.of(
//                    new Alarm("alarm 001", LocalDateTime.now().minusMinutes(1).withSecond(0), "Desc", new Recurrence(RecurrenceType.Once, new boolean[]{false, false, false, false, false, false, false}), new Music(), new Light(), false, true),
                    new Alarm("alarm alarm 002", LocalDateTime.now().plusMinutes(1).withSecond(0), "Desc", new Recurrence(RecurrenceType.EveryDay, new boolean[]{true, true, true, true, true, true, true}), new Music(), new Light(), false, true)//,
//                    new Alarm("alarm 003", LocalDateTime.now().plusMinutes(2).withSecond(0), "Desc", new Recurrence(RecurrenceType.EveryDay, new boolean[]{true, true, true, true, true, false, false}), new Music(), new Light(), false, false),
//                    new Alarm("alarm 005", LocalDateTime.now().plusMinutes(3).withSecond(0), "Desc", new Recurrence(RecurrenceType.Once, new boolean[]{false, false, false, false, false, false, false}), new Music(), new Light(), false, true)
            )) {
                alarmRepository.save(a);
            }
        } catch (Exception e) {
            throw new CustomHttpException("Unable to fill DB with initial data!", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


}
