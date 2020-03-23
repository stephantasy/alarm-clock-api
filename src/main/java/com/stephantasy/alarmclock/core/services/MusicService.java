package com.stephantasy.alarmclock.core.services;

import com.stephantasy.alarmclock.core.models.Music;

import java.io.InputStream;
import java.util.List;

public interface MusicService {
    List<Music> getMusics();
    String play(InputStream file);
    String pause();
    String stop();
    InputStream getRandomSong();
}