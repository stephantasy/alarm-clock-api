package com.stephantasy.alarmclock.dto;

import java.io.Serializable;

public class MusicDto implements Serializable {

    private long id;
    private String name;
    private long delayBeforeFullSound;
    private boolean itLoop;
    private boolean playNext;

    public MusicDto(long id, String name, long delayBeforeFullSound, boolean itLoop, boolean playNext) {
        this.id = id;
        this.name = name;
        this.delayBeforeFullSound = delayBeforeFullSound;
        this.itLoop = itLoop;
        this.playNext = playNext;
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

    public long getDelayBeforeFullSound() {
        return delayBeforeFullSound;
    }

    public void setDelayBeforeFullSound(long delayBeforeFullSound) {
        this.delayBeforeFullSound = delayBeforeFullSound;
    }

    public boolean isItLoop() {
        return itLoop;
    }

    public void setItLoop(boolean itLoop) {
        this.itLoop = itLoop;
    }

    public boolean isPlayNext() {
        return playNext;
    }

    public void setPlayNext(boolean playNext) {
        this.playNext = playNext;
    }
}
