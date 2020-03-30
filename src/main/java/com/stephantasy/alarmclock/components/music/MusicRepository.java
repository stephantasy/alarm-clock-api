package com.stephantasy.alarmclock.components.music;

import com.stephantasy.alarmclock.core.models.Music;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MusicRepository extends CrudRepository<Music, Long> {
    @NotNull
    @Override
    List<Music> findAll();
}
