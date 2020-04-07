package com.stephantasy.alarmclock.components.light;

import com.stephantasy.alarmclock.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DimmerManager implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(DimmerManager.class);

    private final long INTERVAL = 2000;     // milliseconds
    private final int MAX_BRIGHTNESS = 100;

    private final long duration;    // In second
    private final LightMode mode;
    private final int brightnessMax;
    private final DomoticzYeelight domoticzYeelight;
    private final LightParams lightParams;
    private boolean DEBUG;
    private boolean shouldStop;

    public DimmerManager(DomoticzYeelight domoticzYeelight, LightParams lightParams, long duration, boolean debug) {
        this.domoticzYeelight = domoticzYeelight;
        this.lightParams = lightParams;
        this.duration = duration > 0 ? duration : 1;    // Min 1 second
        this.mode = lightParams.getMode();
        this.brightnessMax = lightParams.getBrightness();
        this.DEBUG = debug;
    }

    @Override
    public void run() {

        if(DEBUG) LOG.info("*** Increasing Brightness Started with duration=" + duration + " ***");

        float step = (float) ((float)brightnessMax / duration * INTERVAL / 1000.0);
        float tempStep = step;
        int brightness = (int) tempStep; // number between 0 and 100 (brightest)
        int previousbrightness = brightness;

        while (!shouldStop) {

            // Limit to max
            if (brightness >= brightnessMax) {
                brightness = brightnessMax;
                stopIt();
            } else if (brightness >= MAX_BRIGHTNESS) {
                brightness = MAX_BRIGHTNESS;
                stopIt();
            }

            // Set new volume
            if(previousbrightness != brightness) {
                setValue(brightness);
            }
            previousbrightness = brightness;
            tempStep += step;
            brightness = (int) tempStep;

            if(DEBUG) LOG.info("brightness = " + brightness);
//            if(DEBUG) LOG.info("tempStep = " + tempStep);
//            if(DEBUG) LOG.info("step = " + step);
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        if(DEBUG) LOG.info("*** Increasing Brightness Completed ***");
    }

    private void setValue(int brightness) {
        try {
            lightParams.setBrightness(brightness);
            if(lightParams.isGradient()){
                Color gradientColor = LightGradient.getColor(brightness, lightParams.getColorFrom(), lightParams.getColorTo());
                lightParams.setColor(gradientColor);
                if(DEBUG) LOG.info("gradientColor = " + gradientColor);
            }
            domoticzYeelight.sendNewValues(lightParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopIt() {
        this.shouldStop = true;
        setValue(0);
    }
}
