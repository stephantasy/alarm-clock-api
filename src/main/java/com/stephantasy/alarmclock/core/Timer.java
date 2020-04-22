package com.stephantasy.alarmclock.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Timer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Timer.class);

    private final long INTERVAL = 1000;     // milliseconds

    private TimerCallback endCallback;
    private TimerCallback otherCallback;
    private final long duration;    // In second
    private boolean DEBUG;
    private boolean shouldStop;
    private String name;

    public Timer(String name, long duration, TimerCallback endCallback, boolean DEBUG) {
        this.duration = duration;
        this.endCallback = endCallback;
        this.DEBUG = DEBUG;
        this.name = name;
    }

    public Timer(String name, long duration, TimerCallback endCallback, TimerCallback callback, boolean DEBUG) {
        this.duration = duration;
        this.endCallback = endCallback;
        this.otherCallback = callback;
        this.DEBUG = DEBUG;
        this.name = name;
    }

    @Override
    public void run() {
        if (DEBUG) LOG.info("*** Timer -" + name + "- Started with duration=" + duration + " ***");
        long start = System.currentTimeMillis();
        while (!shouldStop) {
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            if (timeElapsed >= duration * 1000) {
                if (DEBUG) LOG.info("*** Timer -" + name + "- Elapsed after " + timeElapsed / 1000 + " seconds ***");
                break;
            }
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                // ignore
            }
            if(otherCallback != null){
                otherCallback.doIt();
            }
        }
        endCallback.doIt();
        if (DEBUG) LOG.info("*** Time Out! (" + name + ") ***");
    }

    public void stopIt() {
        this.shouldStop = true;
    }
}
