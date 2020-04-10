package com.stephantasy.alarmclock.controllers;

import com.stephantasy.alarmclock.core.services.MusicService;
import com.stephantasy.alarmclock.dto.MusicDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;

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

    @ApiOperation(value = "Get all Music")
    @GetMapping("/")
    public ResponseEntity<List<MusicDto>> getAllMusics() {
        return new ResponseEntity<>(musicService.getMusics(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get the Music")
    @GetMapping("/music/{id}")
    public ResponseEntity<MusicDto> getMusic(@PathVariable long id) {
        return new ResponseEntity<>(musicService.getMusic(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Play a random music")
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

    @ApiOperation(value = "Stop the played music", nickname = "stopMusic")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Stop the played music", response = String.class)
    })
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

    @ApiOperation(value = "Postpone the played music", nickname = "postponeMusic")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Postpone the played music", response = String.class)
    })
    @PutMapping(value = "/postpone", produces = {"application/json"})
    public ResponseEntity<String> postponeMusic() {
        String postpone = musicService.postpone();
        return new ResponseEntity<>(postpone, HttpStatus.OK);
    }

}
