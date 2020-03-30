package com.stephantasy.alarmclock.components.light;

import com.stephantasy.alarmclock.core.*;
import com.stephantasy.alarmclock.core.events.AlarmEvent;
import com.stephantasy.alarmclock.core.exceptions.FetchAlarmException;
import com.stephantasy.alarmclock.core.models.Light;
import com.stephantasy.alarmclock.core.services.LightService;
import com.stephantasy.alarmclock.dto.LightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LightManager implements LightService, ApplicationListener<AlarmEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(LightManager.class);
    @Value("${alarmclock.debug}")
    private boolean DEBUG;

    private LightRepository lightRepository;
    private DomoticzYeelight domoticzYeelight;

    private Runnable dimmer;
    private Runnable timer;


    @Autowired
    public LightManager(LightRepository lightRepository, DomoticzYeelight domoticzYeelight) {
        this.lightRepository = lightRepository;
        this.domoticzYeelight = domoticzYeelight;
    }


    @Override
    public LightDto getLight(long id) {
        Optional<Light> light = lightRepository.findById(id);
        if (light.isPresent()) {
            return light.get().toDto();
        }
        throw new FetchAlarmException("Light not found with id: " + id);
    }

    @Override
    public List<LightDto> getLights() {
        return lightRepository.findAll()
                .stream()
                .map(Light::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public String turnOnAll() {
        return "turnOnAll";
    }

    @Override
    public String turnOffAll() {
        stopDimmer();
        // Stop Timer
        stopTimer();
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

        long id = alarmEvent.getAlarm().getLightID();
        Optional<Light> optLight = lightRepository.findById(id);
        if (!optLight.isPresent()) {
            throw new FetchAlarmException("Light not found with id: " + id);
        }
        Light light = optLight.get();

        if(DEBUG) LOG.info("      => The light " + light.getName() + " is played");

        // Run Dimmer with gradient
        LightParams lightParams = new LightParams(new Color(0, 0, 0), new Color(128, 0, 0));
        lightParams.setBrightness(light.getMaxIntensity());
        lightParams.setMode(LightMode.WHITE);

        // Run dimmer
        stopDimmer();
        dimmer = new DimmerManager(domoticzYeelight, lightParams, light.getDuration(), DEBUG);
        new Thread(dimmer).start();

        // Run Timer (to limit time of running)
        stopTimer();
        timer = new Timer("Light", 3600, this::turnOffAll, DEBUG);
        new Thread(timer).start();
    }

    @Override
    public boolean getState() {
        return (dimmer != null);
    }

    @Override
    public String postpone() {
        // TODO
        // To postpone, we simply save the alarm with 5 more minutes
        return null;
    }

    private void stopDimmer() {
        if (dimmer != null) {
            ((DimmerManager) dimmer).stopIt();
            dimmer = null;
        }
    }

    private void stopTimer() {
        if (timer != null) {
            ((Timer) timer).stopIt();
            timer = null;
        }

    }
}
