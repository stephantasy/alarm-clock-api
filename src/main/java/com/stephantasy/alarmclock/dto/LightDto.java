package com.stephantasy.alarmclock.dto;

import java.io.Serializable;

public class LightDto implements Serializable {
    private long id;
    private String name;
    private int maxIntensity;
    private String color;
    private int duration;

    public LightDto(long id, String name, int maxIntensity, String color, int duration) {
        this.id = id;
        this.name = name;
        this.maxIntensity = maxIntensity;
        this.color = color;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxIntensity() {
        return maxIntensity;
    }

    public void setMaxIntensity(int maxIntensity) {
        this.maxIntensity = maxIntensity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
