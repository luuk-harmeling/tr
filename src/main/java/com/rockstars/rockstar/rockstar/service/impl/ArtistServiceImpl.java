package com.rockstars.rockstar.rockstar.service.impl;

import com.rockstars.rockstar.rockstar.domain.Artist;
import com.rockstars.rockstar.rockstar.domain.mapperObjects.ArtistDto;
import com.rockstars.rockstar.rockstar.repository.ArtistRepository;
import com.rockstars.rockstar.rockstar.service.ArtistService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ArtistServiceImpl implements ArtistService {


    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Artist save(ArtistDto artistDto) {


        if(StringUtils.isNotBlank(artistDto.getName())){
            log.info("Saving artist with name: {} ", artistDto.getName());
            if(!artistExistsByName(artistDto.getName())){
                Artist artist = new Artist(artistDto.getName());
                log.info("Artist with name: {} is new, saving.", artistDto.getName());
                artistRepository.save(artist);
                return artist;
            }
        }else {
            log.info("Given Artist name is empty or null, saving cancelled");
        }
        log.info("Artist with name: {} already exists, saving cancelled.", artistDto.getName());

        return null;
    }


    @Override
    public Artist findArtistByName(String artistName) {
        log.info("Commencing find artist by the name: {}", artistName);
        return artistRepository.findArtistByName(artistName);
    }

    @Override
    public Artist updateArtistInformation(ArtistDto artistDto, Long artistId) {

        Artist artist = artistRepository.findById(artistId).orElse(null);

        if(artist != null) {
            artist.setName(artistDto.getName());
            return artistRepository.save(artist);
        } else {
            return null;
        }
    }

    @Override
    public boolean removeArtist(Long artistId) {
        if (artistRepository.existsById(artistId)) {
            artistRepository.deleteById(artistId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean artistExistsByName(String artistName) {
        return artistRepository.existsArtistByName(artistName);
    }

    @Override
    public boolean areArtistsPresent() {
        return artistRepository.findAll().size() > 0;
    }

}
