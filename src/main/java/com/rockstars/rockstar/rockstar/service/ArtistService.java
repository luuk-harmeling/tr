package com.rockstars.rockstar.rockstar.service;

import com.rockstars.rockstar.rockstar.domain.Artist;
import com.rockstars.rockstar.rockstar.domain.mapperObjects.ArtistDto;


public interface ArtistService {

    Artist save(ArtistDto artistDto);

    Artist findArtistByName(String artistName);

    Artist updateArtistInformation(ArtistDto artistDto, Long artistId);

    boolean removeArtist(Long artistId);

    boolean artistExistsByName(String artistName);

    boolean areArtistsPresent();

}
