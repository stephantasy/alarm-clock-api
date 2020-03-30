package com.stephantasy.alarmclock.core.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stephantasy.alarmclock.core.Recurrence;
import com.stephantasy.alarmclock.dto.AlarmDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    private String description;

    @Column
    @Embedded
    private Recurrence recurrence;

    private long musicID;
    private long lightID;
    private boolean deleteAfterDone;
    private boolean activated;

    public Alarm(String name, LocalDateTime date, String description, Recurrence recurrence, long musicID, long lightID, boolean deleteAfterDone, boolean activated) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.recurrence = recurrence;
        this.musicID = musicID;
        this.lightID = lightID;
        this.deleteAfterDone = deleteAfterDone;
        this.activated = activated;
    }

    public Alarm() {
    }

    public Alarm(AlarmDto alarmDto) {
        this.id = alarmDto.getId();
        this.name = alarmDto.getName();
        this.date = alarmDto.getDate();
        this.description = alarmDto.getDescription();
        this.recurrence = alarmDto.getRecurrence();
        this.musicID = alarmDto.getMusicID();
        this.lightID = alarmDto.getLightID();
        this.deleteAfterDone = alarmDto.isDeleteAfterDone();
        this.activated = alarmDto.isActivated();
    }

    public AlarmDto toDto() {
        return new AlarmDto(
                id,
                name,
                date,
                description,
                recurrence,
                musicID,
                lightID,
                deleteAfterDone,
                activated
        );
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    public long getMusicID() {
        return musicID;
    }

    public void setMusicID(long musicID) {
        this.musicID = musicID;
    }

    public long getLightID() {
        return lightID;
    }

    public void setLightID(long lightID) {
        this.lightID = lightID;
    }

    public boolean isDeleteAfterDone() {
        return deleteAfterDone;
    }

    public void setDeleteAfterDone(boolean deleteAfterDone) {
        this.deleteAfterDone = deleteAfterDone;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}

