package com.stephantasy.alarmclock.core.events;

import com.stephantasy.alarmclock.core.models.Alarm;


public interface AlarmPublisher {
    void publishAlarmEvent(final Alarm alarm);
}
