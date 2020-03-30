package com.stephantasy.alarmclock.core.services;

import com.stephantasy.alarmclock.dto.LightDto;

import java.util.List;

public interface LightService {
    LightDto getLight(long id);
    List<LightDto> getLights();
    String turnOnAll();
    String turnOffAll();
    String turnOnById(String id);
    String turnOffById(String id);
    String dimById(String id, String value);
    boolean getState();
    String postpone();
}
