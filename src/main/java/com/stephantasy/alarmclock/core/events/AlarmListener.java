package com.stephantasy.alarmclock.core.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AlarmListener implements ApplicationListener<AlarmEvent> {

    Logger LOG = LoggerFactory.getLogger(AlarmListener.class);
    @Value("${alarmclock.debug}")
    private boolean DEBUG;

    @Override
    public void onApplicationEvent(AlarmEvent alarmEvent) {

        if (alarmEvent.getAlarm() == null) return;
        if(DEBUG) LOG.info("      => Received spring custom event - " + alarmEvent.getAlarm().getName());
    }
}
