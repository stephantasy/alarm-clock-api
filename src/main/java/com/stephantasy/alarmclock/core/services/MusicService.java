package com.stephantasy.alarmclock.core.services;

import com.stephantasy.alarmclock.dto.MusicDto;

import java.io.InputStream;
import java.util.List;

public interface MusicService {
    MusicDto getMusic(long id);
    List<MusicDto> getMusics();
    String play(InputStream file);
    String pause();
    String stop();
    InputStream getRandomSong();
    boolean getState();
    String postpone();
}