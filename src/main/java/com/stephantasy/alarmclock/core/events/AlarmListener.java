package com.stephantasy.alarmclock.core.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AlarmListener implements ApplicationListener<AlarmEvent> {

    Logger logger = LoggerFactory.getLogger(AlarmListener.class);

    @Override
    public void onApplicationEvent(AlarmEvent alarmEvent) {

        if (alarmEvent.getAlarm() == null) return;
        logger.info("      => Received spring custom event - " + alarmEvent.getAlarm().getName());
    }
}
