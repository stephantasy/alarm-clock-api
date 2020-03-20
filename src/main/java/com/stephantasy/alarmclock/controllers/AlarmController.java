package com.stephantasy.alarmclock.controllers;

import com.stephantasy.alarmclock.core.services.AlarmService;
import com.stephantasy.alarmclock.dto.AlarmDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alarms")
@Api(tags = {"Alarms"})
@CrossOrigin//(origins = "http://localhost:4200")     // For Angular
public class AlarmController {

    Logger logger = LoggerFactory.getLogger(AlarmController.class);
    private AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @ApiOperation(value = "Get a List of all Alarms")
    @GetMapping("")
    public List<AlarmDto> getAlarms() {
        return alarmService.getAlarms();
    }

    @ApiOperation(value = "Get the Specified Alarm")
    @GetMapping("/alarm/{id}")
    public AlarmDto getAlarm(@PathVariable("id") int id) {
        return alarmService.getAlarm(id);
    }

    @ApiOperation(value = "Save the Alarm")
    @PutMapping("/alarm")
    public AlarmDto updateAlarm(@RequestBody AlarmDto alarm) {
        return alarmService.updateAlarm(alarm);
    }

    @ApiOperation(value = "Save a new Alarm")
    @PostMapping("/alarm/add")
    public AlarmDto addAlarm(@RequestBody AlarmDto alarm) {
        return alarmService.addAlarm(alarm);
    }

}
