package com.stephantasy.alarmclock.core.services;

import com.stephantasy.alarmclock.core.models.Light;

import java.util.List;

public interface LightService {
    List<Light> getLights();
    String turnOnAll();
    String turnOffAll();
    String turnOnById(String id);
    String turnOffById(String id);
    String dimById(String id, String value);
    boolean getState();
    String postpone();
}
