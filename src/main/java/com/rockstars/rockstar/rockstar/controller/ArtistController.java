package com.rockstars.rockstar.rockstar.controller;

import com.rockstars.rockstar.rockstar.domain.Artist;
import com.rockstars.rockstar.rockstar.domain.mapperObjects.ArtistDto;
import com.rockstars.rockstar.rockstar.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;


@RestController
@RequestMapping("/rest/artist/")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping(path = "/{artistName}", produces = "application/json")
    public ResponseEntity<Artist> getArtistByName(@PathVariable @NotNull String artistName){

        return Optional
                .ofNullable(artistService.findArtistByName(artistName))
                .map(artist -> ResponseEntity.ok(artist))
                .orElseGet(() -> ResponseEntity.noContent().build());

    }

    @PostMapping(path = "/",consumes = "application/json",produces = "application/json")
    public ResponseEntity<Object> addArtist(@RequestBody @NotNull ArtistDto artistDto) {

        ResponseEntity response;

        if(artistService.save(artistDto) != null) {
            try {
                response = ResponseEntity.created(new URI("/rest/artist/")).build();
            } catch (URISyntaxException e) {
                response = new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            response = new ResponseEntity("The given artist already exists",HttpStatus.CONFLICT);
        }

        return response;
    }

    @DeleteMapping(path = "/{artistId}" )
    public ResponseEntity<Object> deleteArtist(@PathVariable @NotNull Long artistId) {

        ResponseEntity response;

        if (artistService.removeArtist(artistId)) {
            response = ResponseEntity.noContent().build();
        } else {
            response = ResponseEntity.notFound().build();
        }

        return response;

    }

    //Note: Since the assignment stated "to update ALL properties", I've deceided to go for PUT instead of PATCH.
    // PUT should be used for FULL updates
    // PATCH should be used for one (or more) fields, but not ALL fields.
    @RequestMapping(path = "/{artistId}",consumes = "application/json",produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateArtist(@PathVariable @NotNull Long artistId, @RequestBody @NotNull ArtistDto artistDto) {

        ResponseEntity response;

        if(artistService.updateArtistInformation(artistDto, artistId) != null) {
            response = ResponseEntity.ok().build();
        } else {
            response = ResponseEntity.notFound().build();
        }


        return response;

    }


}
