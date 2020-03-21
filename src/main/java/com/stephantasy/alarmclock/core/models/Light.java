package com.stephantasy.alarmclock.core.models;

import javax.persistence.Embeddable;

@Embeddable
public class Light {

    private String name;
    private int maxIntensity;
    private String color;
    private int duration;

    public Light() {
        name = "default";
        maxIntensity = 100;
        color = "0";
        duration = 15 * 60;
    }

    public Light(String name, int maxIntensity, String color, int duration) {
        this.name = name;
        this.maxIntensity = maxIntensity;
        this.color = color;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public int getMaxIntensity() {
        return maxIntensity;
    }

    public String getColor() {
        return color;
    }

    public int getDuration() {
        return duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxIntensity(int maxIntensity) {
        this.maxIntensity = maxIntensity;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
