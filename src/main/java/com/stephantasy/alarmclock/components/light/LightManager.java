package com.stephantasy.alarmclock.components.light;

import com.stephantasy.alarmclock.core.DomoticzYeelight;
import com.stephantasy.alarmclock.core.LightMode;
import com.stephantasy.alarmclock.core.LightParams;
import com.stephantasy.alarmclock.core.events.AlarmEvent;
import com.stephantasy.alarmclock.core.models.Light;
import com.stephantasy.alarmclock.core.services.LightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LightManager implements LightService, ApplicationListener<AlarmEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(LightManager.class);

    private Runnable t;

    private DomoticzYeelight domoticzYeelight;

    @Autowired
    public LightManager(DomoticzYeelight domoticzYeelight) {
        this.domoticzYeelight = domoticzYeelight;
    }


    @Override
    public List<Light> getLights() {
        return null;
    }

    @Override
    public String turnOnAll() {
        return "turnOnAll";
    }

    @Override
    public String turnOffAll() {
        stopDimmer();
        return domoticzYeelight.sendStop();
    }

    @Override
    public String turnOnById(String id) {
        return "turnOnById";
    }

    @Override
    public String turnOffById(String id) {
        return "turnOffById";
    }

    @Override
    public String dimById(String id, String value) {
        return "dimById";
    }

    @Override
    public void onApplicationEvent(AlarmEvent alarmEvent) {


        Light light = alarmEvent.getAlarm().getLight();
        LOG.info("      => The light " + light.getName() + " is played");

        // Run Dimmer
        light.setDuration(15*60); // 15 minutes
        LightParams lightParams = new LightParams();
        lightParams.setBrightness(light.getMaxIntensity());
        lightParams.setMode(LightMode.WHITE);
        lightParams.setColor1(128);
        lightParams.setColor2(0);
        lightParams.setColor3(0);

        stopDimmer();
        t = new DimmerManager(domoticzYeelight, lightParams, light.getDuration());
        new Thread(t).start();

    }


    private void stopDimmer(){
        if(t != null) {
            ((DimmerManager) t).stopIt();
            t = null;
        }
    }
}
