package com.stephantasy.alarmclock.core.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stephantasy.alarmclock.core.Recurrence;
import com.stephantasy.alarmclock.dto.AlarmDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    private String description;

    @Column
    @Embedded
    private Recurrence recurrence;

    @Column
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "music_name"))
    })
    private Music music;

    @Column
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "light_name"))
    })
    private Light light;

    private boolean deleteAfterDone;
    private boolean activated;

    public Alarm(String name, LocalDateTime date, String description, Recurrence recurrence, Music music, Light light, boolean deleteAfterDone, boolean activated) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.recurrence = recurrence;
        this.music = music;
        this.light = light;
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
        this.music = alarmDto.getMusic() == null ? new Music() : alarmDto.getMusic();
        this.light = alarmDto.getLight() == null ? new Light() : alarmDto.getLight();
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
                music,
                light,
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

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
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

