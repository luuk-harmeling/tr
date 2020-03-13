package com.rockstars.rockstar.rockstar.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockstars.rockstar.rockstar.domain.mapperObjects.ArtistDto;
import com.rockstars.rockstar.rockstar.domain.mapperObjects.SongDto;
import com.rockstars.rockstar.rockstar.service.ArtistService;
import com.rockstars.rockstar.rockstar.service.SongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Profile("dev")
@Slf4j
public class JsonDocumentParser implements ApplicationListener<ApplicationReadyEvent> {

    //log log = logFactory.getlog(JsonDocumentParser.class);

    private static final String GENRE_METAL = "metal";

    private static final int MUSIC_YEAR = 2016;

    private final ArtistService artistService;

    private final SongService songService;

    @Autowired
    private JsonDocumentParser(ArtistService artistService, SongService songService) {
        this.artistService = artistService;
        this.songService = songService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if(!artistService.areArtistsPresent() && !songService.areSongsPresent()){
            log.info("Database is empty, commencing json file read and persist");
            performReadAndPersistFromFiles();
        } else {
            log.info("Database filled, skipping json file read and persist");
        }
    }

    private void performReadAndPersistFromFiles(){
        ObjectMapper objectMapper = new ObjectMapper();
        //Properties in Json files start with capital letters, we don't want that for our properties
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        //Since we let hibernate be the boss of Id's (see readme for full explanation), we add this configuration so the parser doesn't fail on the ID field.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        File songsJson = new File("songs.json");
        File artistsJson = new File("artists.json");

        try {
            List<SongDto> songs = objectMapper.readValue(songsJson, new TypeReference<List<SongDto>>() {});
            List<ArtistDto> artists = objectMapper.readValue(artistsJson, new TypeReference<List<ArtistDto>>() {});

            filterAndPersistArtistsAndSongs(artists, songs);

        } catch (IOException e) {
            log.error("Something went horribly wrong parsing reading and/or parsing the json files, error: {}", e.getMessage());
        }
    }

    private void filterAndPersistArtistsAndSongs(List<ArtistDto> artistDtos, List<SongDto> songDtos){

        List<SongDto> genreAndYearFilteredSongs = songDtos.stream().filter(songDto -> songDto.getGenre().toLowerCase().contains(GENRE_METAL) && songDto.getYear() < MUSIC_YEAR).collect(Collectors.toList());

        //This list could be in the genreFilteredArtists lambda but I find this more readable.
        List<String> artistNames = genreAndYearFilteredSongs.stream().map(SongDto::getArtist).collect(Collectors.toList());

        persistArtists( artistDtos.stream().filter(artistDto -> artistNames.contains(artistDto.getName())).collect(Collectors.toList()));

        persistSongs(genreAndYearFilteredSongs);

    }

    private void persistArtists(List<ArtistDto> artistDtos){
        for (ArtistDto artistDto : artistDtos) {
            artistService.save(artistDto);
        }
    }

    private void persistSongs(List<SongDto> songs){
        for (SongDto song : songs) {
            songService.save(song);
        }
    }




}
