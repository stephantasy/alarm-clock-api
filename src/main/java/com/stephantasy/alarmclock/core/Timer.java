package com.stephantasy.alarmclock.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Timer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Timer.class);

    private final long INTERVAL = 1000;     // milliseconds

    private TimerCallback callback;
    private final long duration;    // In second
    private boolean DEBUG;
    private boolean shouldStop;

    public Timer(long duration, TimerCallback callback, boolean DEBUG) {
        this.duration = duration;
        this.callback = callback;
        this.DEBUG = DEBUG;
    }

    @Override
    public void run() {
        if(DEBUG) LOG.info("*** Timer Started with duration=" + duration + " ***");
        long start = System.currentTimeMillis();
        while (!shouldStop) {
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            if(timeElapsed >= duration*1000){
                if(DEBUG) LOG.info("*** Timer Elapsed after " + timeElapsed/1000 + " seconds ***");
                break;
            }
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        callback.timeout();
        if(DEBUG) LOG.info("*** Time Out! ***");
    }

    public void stopIt() {
        this.shouldStop = true;
    }
}