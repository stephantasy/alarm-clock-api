package com.stephantasy.alarmclock.controllers;

import com.stephantasy.alarmclock.core.services.MusicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/musics")
@Api(tags = {"Musics"})
@CrossOrigin
public class MusicController {

    private MusicService musicService;

    @Autowired
    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @ApiOperation(value = "Play the ... music")
    @PutMapping("/start")
    public String startMusic() {
        InputStream file;
        file = musicService.getRandomSong();
        return musicService.play(file);
    }

    @ApiOperation(value = "Pause the played music")
    @PutMapping("/pause")
    public String pauseMusic() {
        return musicService.pause();
    }

    @ApiOperation(value = "Stop the played music", nickname = "stopMusic", notes = "Stop the playing music", response = String.class, tags = {"stopMusic", "stop", "music"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Audit trail file under PDF format", response = String.class)})
    @PutMapping(value = "/stop", produces = {"application/json"})
    public ResponseEntity<String> stopMusic() {
        String stop = musicService.stop();
        return new ResponseEntity<>(stop, HttpStatus.OK);
    }

    @ApiOperation(value = "Is the Music On?")
    @GetMapping("/state")
    public ResponseEntity<Boolean> getMusicState() {
        boolean isOn = musicService.getState();
        return new ResponseEntity<>(isOn, HttpStatus.OK);
    }

}
