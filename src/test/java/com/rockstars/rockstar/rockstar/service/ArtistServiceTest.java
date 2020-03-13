package com.rockstars.rockstar.rockstar.service;

import com.rockstars.rockstar.rockstar.domain.Artist;
import com.rockstars.rockstar.rockstar.domain.mapperObjects.ArtistDto;
import com.rockstars.rockstar.rockstar.repository.ArtistRepository;
import com.rockstars.rockstar.rockstar.service.impl.ArtistServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
public class ArtistServiceTest {

    private static final String ARTIST_WITH_NAME_ARTIST = "Artist";

    private static final String ARTIST_WITH_NAME_OTHER = "Other";

    @Autowired
    private ArtistServiceImpl artistService;

    @Autowired
    private ArtistRepository artistRepository;


    @Test
    @Transactional
    public void testCreateArtist(){

        ArtistDto artistDto = new ArtistDto();
        artistDto.setName(ARTIST_WITH_NAME_ARTIST);

        artistService.save(artistDto);

        Artist artistByName = artistService.findArtistByName(ARTIST_WITH_NAME_ARTIST);

        assertTrue(artistByName.getName().equalsIgnoreCase(ARTIST_WITH_NAME_ARTIST));
    }

    @Test
    @Transactional
    public void testMultipleArtistAddingFails(){

        ArtistDto firstArtistDto = new ArtistDto();
        firstArtistDto.setName(ARTIST_WITH_NAME_ARTIST);

        ArtistDto secondArtistDto = new ArtistDto();
        secondArtistDto.setName(ARTIST_WITH_NAME_ARTIST);

        artistService.save(firstArtistDto);
        //Our service doesn't allow for duplicates so it should not save this record.
        artistService.save(secondArtistDto);

        //Only one record should be present at this point.
        assertEquals(1, artistRepository.findAll().size());

    }

    @Test
    @Transactional
    public void testUpdateArtistInformation(){
        ArtistDto artistDto = new ArtistDto();
        artistDto.setName(ARTIST_WITH_NAME_ARTIST);

        final Artist savedArtist = artistService.save(artistDto);

        ArtistDto newArtist = new ArtistDto();
        newArtist.setName(ARTIST_WITH_NAME_OTHER);

        //At this point the name should match |ARTIST_WITH_NAME_ARTIST|
        assertEquals(ARTIST_WITH_NAME_ARTIST, savedArtist.getName());

        artistService.updateArtistInformation(newArtist, savedArtist.getId());

        //At this point the name should match |ARTIST_WITH_NAME_OTHER|

        Artist afterUpdate = artistRepository.findById(savedArtist.getId()).orElse(null);

        assertNotNull(afterUpdate);

        assertEquals(ARTIST_WITH_NAME_OTHER, afterUpdate.getName());

    }

    @Test
    @Transactional
    public void testDeleteArtist(){
        ArtistDto artistDto = new ArtistDto();
        artistDto.setName(ARTIST_WITH_NAME_ARTIST);

        final Artist savedArtist = artistService.save(artistDto);

        assertEquals(1, artistRepository.findAll().size());

        artistService.removeArtist(savedArtist.getId());

        assertEquals(0, artistRepository.findAll().size());

    }


}

