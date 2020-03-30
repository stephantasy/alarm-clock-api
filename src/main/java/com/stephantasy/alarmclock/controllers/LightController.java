package com.stephantasy.alarmclock.controllers;

import com.stephantasy.alarmclock.core.services.LightService;
import com.stephantasy.alarmclock.dto.LightDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @ApiOperation(value = "Get All Light")
    @GetMapping("/")
    public ResponseEntity<List<LightDto>> getAllLights() {
        return new ResponseEntity<>(lightService.getLights(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get the Light")
    @GetMapping("/light/{id}")
    public ResponseEntity<LightDto> getLight(@PathVariable long id) {
        return new ResponseEntity<>(lightService.getLight(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Turn All Lights On")
    @PutMapping("/on-all")
    public ResponseEntity<String> turnOnAll() {
        return new ResponseEntity<>(lightService.turnOnAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Turn All Lights Off")
    @PutMapping("/off-all")
    public ResponseEntity<String> turnOffAll() {
        return new ResponseEntity<>(lightService.turnOffAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Turn a Light On")
    @PutMapping("/{id}/on}")
    public ResponseEntity<String> turnOn(@PathVariable String id) {
        return new ResponseEntity<>(lightService.turnOnById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Turn a Light Off")
    @PutMapping("/{id}/off")
    public ResponseEntity<String> turnOff(@PathVariable String id) {
        return new ResponseEntity<>(lightService.turnOffById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Dim a Light")
    @PutMapping("/{id}/dim")
    public ResponseEntity<String> dim(@PathVariable String id, @PathVariable String value) {
        return new ResponseEntity<>(lightService.dimById(id, value), HttpStatus.OK);
    }

    // TODO: by ID!!
    @ApiOperation(value = "Is the Light On?")
    @GetMapping("/state")
    public ResponseEntity<Boolean> getLightState() {
        boolean isOn = lightService.getState();
        return new ResponseEntity<>(isOn, HttpStatus.OK);
    }


    // TODO: by ID!!
    @ApiOperation(value = "Postpone the current Light")
    @PutMapping("/light/postpone")
    public ResponseEntity<String> postponeLight() {
        return new ResponseEntity<>(lightService.postpone(), HttpStatus.OK);
    }
}
