package com.stephantasy.alarmclock.core.models;

import javax.persistence.Embeddable;

@Embeddable
public class Music {

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
