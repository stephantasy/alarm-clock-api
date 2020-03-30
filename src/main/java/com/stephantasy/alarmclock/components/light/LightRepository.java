package com.stephantasy.alarmclock.components.light;

import com.stephantasy.alarmclock.core.models.Light;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LightRepository extends CrudRepository<Light, Long> {
    @NotNull
    @Override
    List<Light> findAll();
}
