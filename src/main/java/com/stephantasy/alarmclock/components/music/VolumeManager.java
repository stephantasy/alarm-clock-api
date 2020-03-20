package com.stephantasy.alarmclock.components.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class VolumeManager extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(VolumeManager.class);

    private double STEP = 0.01D;    // to increment gain from 0 to 1
    private double MAX_GAIN = 0.999D;

    private long duration;
    private FloatControl gainControl;
    private boolean shouldStop;

    public VolumeManager(Clip clip, long duration) {
        this.duration = duration > 0 ? duration : 1;    // Min 1
        this.gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    }

    @Override
    public void run() {

//        System.out.println("*** Increasing Volume Started with duration=" + duration + "***");

        double gain = STEP; // number between 0 and 1 (loudest)
        long interval = (long) (duration * 1000 * STEP);

        while (!shouldStop) {

            // Convert Gain in Decibel
            float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);

            // Limit to 1
            if (gain > MAX_GAIN) {
                gain = 1D;
                stopIt();
            }

            // Limit to Max volume (but it appears that the max volume is higher than the max gain, making head-phones saturate)
            if (dB > gainControl.getMaximum()) {
                dB = gainControl.getMaximum();
                stopIt();
            }

            // Set new volume
            gainControl.setValue(dB);
            gain += STEP;

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // ignore
            }
        }
//        System.out.println("*** Increasing Volume Completed ***");
    }

    public void stopIt() {
        this.shouldStop = true;
    }
}
