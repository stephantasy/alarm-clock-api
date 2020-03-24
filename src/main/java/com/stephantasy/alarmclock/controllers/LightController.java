package com.stephantasy.alarmclock.controllers;

import com.stephantasy.alarmclock.core.services.LightService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lights")
@Api(tags = {"Lights"})
@CrossOrigin
public class LightController {
    private LightService lightService;

    @Autowired
    public LightController(LightService lightService) {
        this.lightService = lightService;
    }

    @ApiOperation(value = "Turn On All Lights")
    @GetMapping("/on-all")
    public String turnOnAll() {
        lightService.turnOnAll();
        return "TODO";
    }

    @ApiOperation(value = "Turn Off All Lights")
    @PutMapping("/off-all")
    public ResponseEntity<String> turnOffAll() {
        return new ResponseEntity<>(lightService.turnOffAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Turn On a Light")
    @GetMapping("/on")
    public String turnOn(@PathVariable String id) {
        lightService.turnOnById(id);
        return "TODO";
    }

    @ApiOperation(value = "Turn Off a Light")
    @GetMapping("/off")
    public String turnOff(@PathVariable String id) {
        lightService.turnOffById(id);
        return "TODO";
    }

    @ApiOperation(value = "Turn On All Lights")
    @GetMapping("/all-on")
    public String dim(@PathVariable String id, @PathVariable String value) {
        lightService.dimById(id, value);
        return "TODO";
    }

    @ApiOperation(value = "Is the Light On?")
    @GetMapping("/state")
    public ResponseEntity<Boolean> getMusicState() {
        boolean isOn = lightService.getState();
        return new ResponseEntity<>(isOn, HttpStatus.OK);
    }
}
