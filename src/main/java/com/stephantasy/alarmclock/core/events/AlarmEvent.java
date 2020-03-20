package com.stephantasy.alarmclock.core.events;

import com.stephantasy.alarmclock.core.models.Alarm;
import org.springframework.context.ApplicationEvent;

public class AlarmEvent extends ApplicationEvent {
    private Alarm alarm;

    public AlarmEvent(Object source, Alarm alarm) {
        super(source);
        this.alarm = alarm;
    }

    public Alarm getAlarm() {
        return alarm;
    }
}