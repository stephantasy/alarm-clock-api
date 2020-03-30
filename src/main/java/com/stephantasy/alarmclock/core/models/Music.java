package com.stephantasy.alarmclock.core.models;

import com.stephantasy.alarmclock.dto.MusicDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private long delayBeforeFullSound;
    private boolean itLoop;
    private boolean playNext;

    public Music() {
        name = "default";
        delayBeforeFullSound = 15 * 60; // In second
        itLoop = true;
        playNext = false;
    }

    public Music(String name, long delayBeforeFullSound, boolean itLoop, boolean playNext) {
        this.name = name;
        this.delayBeforeFullSound = delayBeforeFullSound;
        this.itLoop = itLoop;
        this.playNext = playNext;
    }

    public MusicDto toDto() {
        return new MusicDto(
                id,
                name,
                delayBeforeFullSound,
                itLoop,
                playNext
        );
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDelayBeforeFullSound(long delayBeforeFullSound) {
        this.delayBeforeFullSound = delayBeforeFullSound;
    }

    public void setItLoop(boolean itLoop) {
        this.itLoop = itLoop;
    }

    public void setPlayNext(boolean playNext) {
        this.playNext = playNext;
    }

    public String getName() {
        return name;
    }

    public long getDelayBeforeFullSound() {
        return delayBeforeFullSound;
    }

    public boolean getItLoop() {
        return itLoop;
    }

    public boolean getPlayNext() {
        return playNext;
    }
}
