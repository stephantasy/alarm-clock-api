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
    private Thread dimmerThread, timerThread;


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

        stopDimmer();
        stopTimer();

        // Run dimmer (to start the light gently)
        dimmer = new DimmerManager(domoticzYeelight, lightParams, light.getDuration(), DEBUG);
        // Run Timer (to limit time of running)
        timer = new Timer("Light", 3600, this::turnOffAll, DEBUG);

        dimmerThread = new Thread(dimmer, "Light Dimmer");
        timerThread = new Thread(timer, "Light Timer");
        dimmerThread.start();
        timerThread.start();
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
            try {
                dimmer = null;
                dimmerThread.join();
            } catch (InterruptedException e) {
                LOG.error(e.getMessage());
            }
            //dimmer = null;
        }
    }

    private void stopTimer() {
        if (timer != null) {
            ((Timer) timer).stopIt();
            try {
                timer = null;
                timerThread.join();
            } catch (InterruptedException e) {
                LOG.error(e.getMessage());
            }
        }
    }

//    static void popo(){
//        stopDimmer();
//        stopTimer();
//
//        // Run dimmer (to start the light gently)
//        LightParams lightParams = new LightParams(new Color(0, 0, 0), new Color(128, 0, 0));
//        dimmer = new DimmerManager(domoticzYeelight, lightParams, 3, DEBUG);
//        //new Thread(dimmer).start();
//        // Run Timer (to limit time of running)
//        timer = new Timer("Light", 3600, this::turnOffAll, DEBUG);
//        //new Thread(timer).start();
//
//        t1 = new Thread(dimmer, "Dimmer");
//        t2 = new Thread(timer, "Timer");
//
//        t1.start();
//        t2.start();
//    }
//    public static void main(String[] args) {
//
//        Alarm alarm = new Alarm("alarm alarm 002", LocalDateTime.now().plusMinutes(1).withSecond(0), "Desc", new Recurrence(RecurrenceType.EveryDay, new boolean[]{true, true, true, true, true, true, true}), 1, 1, false, true)//,
//        AlarmEvent event = new AlarmEvent(null, alarm);
//
//        popo(event);
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        popo(event);
//    }
}


