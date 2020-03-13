package com.rockstars.rockstar.rockstar.service;
import com.rockstars.rockstar.rockstar.domain.Song;
import com.rockstars.rockstar.rockstar.domain.mapperObjects.SongDto;


import java.util.List;


public interface SongService {

    Song save(SongDto songDto);

    Song updateSongInformation(SongDto songDto, Long songId);

    boolean areSongsPresent();

    boolean removeSong(Long songId);

    boolean songExistsByName(String songName);

    List<Song> findSongsByGenre(String genre);
}
