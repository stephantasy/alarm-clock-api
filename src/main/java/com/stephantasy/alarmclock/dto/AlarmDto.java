package com.stephantasy.alarmclock.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stephantasy.alarmclock.core.Recurrence;
import com.stephantasy.alarmclock.core.models.Light;
import com.stephantasy.alarmclock.core.models.Music;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AlarmDto implements Serializable {
    private long id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;
    private String description;
    private Recurrence recurrence;
    private Music music;
    private Light light;
    private boolean deleteAfterDone;
    private boolean activated;


    public AlarmDto(long id, String name, LocalDateTime date, String description, Recurrence recurrence, Music music, Light light, boolean deleteAfterDone, boolean activated) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
        this.recurrence = recurrence;
        this.music = music;
        this.light = light;
        this.deleteAfterDone = deleteAfterDone;
        this.activated = activated;
    }

    public AlarmDto() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public void setDeleteAfterDone(boolean deleteAfterDone) {
        this.deleteAfterDone = deleteAfterDone;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public Music getMusic() {
        return music;
    }

    public Light getLight() {
        return light;
    }

    public boolean isDeleteAfterDone() {
        return deleteAfterDone;
    }

    public boolean isActivated() {
        return activated;
    }
}
