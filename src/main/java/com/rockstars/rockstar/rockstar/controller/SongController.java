package com.rockstars.rockstar.rockstar.controller;

import com.rockstars.rockstar.rockstar.domain.Song;
import com.rockstars.rockstar.rockstar.domain.mapperObjects.SongDto;
import com.rockstars.rockstar.rockstar.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/song/")
public class SongController {

    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping(path = "/{genre}", produces = "application/json")
    public ResponseEntity<List<Song>> getSongsByGenre(@PathVariable @NotNull String genre){

        return Optional
                .ofNullable(songService.findSongsByGenre(genre))
                .map(songs -> ResponseEntity.ok(songs))
                .orElseGet(() -> ResponseEntity.noContent().build());

    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addSong(@RequestBody @NotNull SongDto songDto){

        ResponseEntity response;

        if(songService.save(songDto) != null) {
            try {
                response = ResponseEntity.created(new URI("/rest/song/")).build();
            } catch (URISyntaxException e) {
                response = new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            response = new ResponseEntity("The given song already exists",HttpStatus.CONFLICT);
        }
        return response;
    }

    @DeleteMapping(path = "/{songId}" )
    public ResponseEntity<Object> deleteSong(@PathVariable @NotNull Long songId) {

        ResponseEntity response;

        if (songService.removeSong(songId)) {
            response = ResponseEntity.noContent().build();
        } else {
            response = ResponseEntity.notFound().build();
        }

        return response;
    }

    //Note: Since the assignment stated "to update ALL properties", I've deceided to go for PUT instead of PATCH.
    // PUT should be used for FULL updates
    // PATCH should be used for one (or more) fields, but not ALL fields.
    @PutMapping(path = "/{songId}")
    public ResponseEntity<Object> updateSong(@RequestBody @NotNull SongDto songDto, @PathVariable @NotNull Long songId) {

        ResponseEntity response;

        if(songService.updateSongInformation(songDto, songId) != null){
            response = ResponseEntity.ok().build();
        } else {
            response = ResponseEntity.notFound().build();
        }

        return response;
    }



}

