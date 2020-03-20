package com.stephantasy.alarmclock.components.alarms;

import com.stephantasy.alarmclock.core.models.Alarm;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlarmRepository extends CrudRepository<Alarm, Long> {

    List<Alarm> findAllByOrderByDateAsc();
}
