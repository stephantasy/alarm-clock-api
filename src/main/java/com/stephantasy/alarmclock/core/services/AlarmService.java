package com.stephantasy.alarmclock.core.services;

import com.stephantasy.alarmclock.dto.AlarmDto;

import java.util.List;

public interface AlarmService {

    List<AlarmDto> getAlarms();

    AlarmDto getAlarm(long id);

    AlarmDto addAlarm(AlarmDto alarm);

    AlarmDto updateAlarm(AlarmDto alarm);

    void deleteAlarm(long id);

    void alarmScheduler();

    AlarmDto postponeAlarm(AlarmDto alarm);
}
