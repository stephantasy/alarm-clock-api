package com.stephantasy.alarmclock.components.alarms;

import com.stephantasy.alarmclock.core.RecurrenceType;
import com.stephantasy.alarmclock.core.events.AlarmEvent;
import com.stephantasy.alarmclock.core.events.AlarmPublisher;
import com.stephantasy.alarmclock.core.exceptions.FetchAlarmException;
import com.stephantasy.alarmclock.core.models.Alarm;
import com.stephantasy.alarmclock.core.services.AlarmService;
import com.stephantasy.alarmclock.dto.AlarmDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MySqlAlarmService implements AlarmService, AlarmPublisher {

    Logger LOG = LoggerFactory.getLogger(MySqlAlarmService.class);
    @Value("${alarmclock.debug}")
    private boolean DEBUG;

    private AlarmRepository alarmRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public MySqlAlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Override
    public List<AlarmDto> getAlarms() {
        return alarmRepository.findAllByOrderByDateAsc()
                .stream()
                .map(Alarm::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AlarmDto getAlarm(long id) {
        Optional<Alarm> alarm = alarmRepository.findById(id);
        if (alarm.isPresent()) {
            return alarm.get().toDto();
        }
        throw new FetchAlarmException("Alarm not found with id: " + id);
    }

    @Override
    public AlarmDto addAlarm(AlarmDto alarmDto) {
        Alarm alarm = new Alarm(alarmDto);
        return alarmRepository.save(alarm).toDto();
    }

    @Override
    public AlarmDto updateAlarm(AlarmDto alarmDto) {
        Alarm alarm = new Alarm(alarmDto);
        fixDate(alarm);
        return alarmRepository.save(alarm).toDto();
    }

    private void fixDate(Alarm alarm) {

        int hourAlarm = alarm.getDate().getHour();
        int minuteAlarm = alarm.getDate().getMinute();
        int hourNow = LocalDateTime.now().getHour();
        int minuteNow = LocalDateTime.now().getMinute();

        // If hour and minute are more than Now, we set the date to today
        // (maybe it has been set to tomorrow because the alarm has been already triggered)
        if (hourAlarm >= hourNow && minuteAlarm > minuteNow) {
            alarm.setDate(LocalDateTime.now().withHour(hourAlarm).withMinute(minuteAlarm).withSecond(0));
        }
    }

    @Override
    public void deleteAlarm(long id) {
        alarmRepository.deleteById(id);
    }

    @Override
    @Scheduled(cron = "${alarmclock.scheduling}", zone = "Europe/Luxembourg")
    public void alarmScheduler() {

        if(DEBUG) LOG.info("Alarm Trigger : " + LocalDateTime.now().toString());

        List<Alarm> alarms = alarmRepository.findAllByOrderByDateAsc();
        for (Alarm alarm : alarms) {
            // We cope with older alarm only (Now + 30s)
            if (alarm.getDate().isBefore(LocalDateTime.now().plusSeconds(30))) {
                // The alarm's Date and Time is (Now - 30s)
                if (alarm.isActivated() && alarm.getDate().isAfter(LocalDateTime.now().minusSeconds(30)) && checkIfTriggerable(alarm)) {

                    if(DEBUG) LOG.info("   - Alarm " + alarm.getId() + " was triggered (" + alarm.getDate().toString() + ")");

                    if(alarm.getRecurrence().getRecurrenceType() == RecurrenceType.Once) {
                        alarm.setActivated(false);
                    }
                    alarmRepository.save(alarm);
                    publishAlarmEvent(alarm);

                } else {
                    // Reset the alarm by postponing it to the next day (so it could be fired again tomorrow)
                    alarm.setDate(alarm.getDate().plusDays(1));
                    alarmRepository.save(alarm);

                    if(DEBUG) LOG.info("   - Alarm " + alarm.getId() + " has been reset to : " + alarm.getDate().toString());
                }
            }
        }
    }

    @Override
    public AlarmDto postponeAlarm(AlarmDto alarm) {
        // TODO
        return null;
    }

    private boolean checkIfTriggerable(Alarm alarm) {

        boolean okToBeTriggered;

        // Once => deactivate alarm
        if (alarm.getRecurrence().getRecurrenceType() == RecurrenceType.Once) {
            okToBeTriggered = true;
        }
        // Recurrence
        else {
            int currentDay = LocalDateTime.now().getDayOfWeek().getValue() - 1;
            okToBeTriggered = alarm.getRecurrence().getDays()[currentDay];
        }

        return alarm.isActivated() && okToBeTriggered;
    }


    @Override
    public void publishAlarmEvent(Alarm alarm) {
        AlarmEvent alarmEvent = new AlarmEvent(this, alarm);
        applicationEventPublisher.publishEvent(alarmEvent);
    }
}
