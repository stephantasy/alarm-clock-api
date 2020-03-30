package com.stephantasy.alarmclock.core.models;

import com.stephantasy.alarmclock.dto.LightDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Light {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private int maxIntensity;
    private String color;
    private int duration;

    public Light() {
        name = "default";
        maxIntensity = 100;
        color = "0";
        duration = 15 * 60; // In minute
    }

    public Light(String name, int maxIntensity, String color, int duration) {
        this.name = name;
        this.maxIntensity = maxIntensity;
        this.color = color;
        this.duration = duration;
    }

    public LightDto toDto() {
        return new LightDto(
                id,
                name,
                maxIntensity,
                color,
                duration
        );
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
