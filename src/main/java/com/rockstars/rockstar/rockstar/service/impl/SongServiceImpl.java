package com.rockstars.rockstar.rockstar.service.impl;

import com.rockstars.rockstar.rockstar.domain.Artist;
import com.rockstars.rockstar.rockstar.domain.Song;
import com.rockstars.rockstar.rockstar.domain.mapperObjects.SongDto;
import com.rockstars.rockstar.rockstar.repository.ArtistRepository;
import com.rockstars.rockstar.rockstar.repository.SongRepository;
import com.rockstars.rockstar.rockstar.service.SongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    private final ArtistRepository artistRepository;

    @Autowired
    public SongServiceImpl(SongRepository songRepository, ArtistRepository artistRepository) {
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
    }


    @Override
    public Song save(SongDto songDto) {
        log.info("Saving song with name: {}", songDto.getName());

        Song toPersistSong = new Song();
        toPersistSong.setName(songDto.getName());
        toPersistSong.setYear(songDto.getYear());
        toPersistSong.setShortName(songDto.getShortName());
        toPersistSong.setBpm(songDto.getBpm());
        toPersistSong.setDuration(songDto.getDuration());
        toPersistSong.setMusicGenre(songDto.getGenre());
        toPersistSong.setSpotifyId(songDto.getSpotifyId());
        toPersistSong.setAlbum(songDto.getAlbum());


        Artist correspondingArtist = artistRepository.findArtistByName(songDto.getArtist());

        if (correspondingArtist != null) {
            toPersistSong.setArtist(correspondingArtist);
            //At this point we have established that the artist exists, therefore if the name of song also is existing we should not add it because it whould be a duplicate (assuming the band did not make 2 identical titles in seperate years
            if(songExistsByName(songDto.getName())){
                log.info("Song with name {} already exists", songDto.getName());
                return null;
            }
        } else {
            Artist artist = new Artist(songDto.getArtist());
            artistRepository.save(artist);
            toPersistSong.setArtist(artist);
        }

        songRepository.save(toPersistSong);

        return toPersistSong;
    }

    @Override
    public Song updateSongInformation(SongDto songDto, Long songId) {

        Song song = songRepository.findById(songId).orElse(null);

        if(song != null) {
            Artist artist = artistRepository.findArtistByName(songDto.getArtist());
            if(artist != null) {
                song.setArtist(artist);
            } else {
                //Newly given artist does not exist thus we can't update the artist for this song.
                return null;
            }
            song.setName(songDto.getName());
            song.setShortName(songDto.getShortName());
            song.setYear(songDto.getYear());
            song.setBpm(songDto.getBpm());
            song.setDuration(songDto.getDuration());
            song.setMusicGenre(songDto.getGenre());
            song.setSpotifyId(songDto.getSpotifyId());
            song.setAlbum(songDto.getAlbum());

            return songRepository.save(song);
        }
        return null;
    }

    @Override
    public boolean areSongsPresent() {
        return songRepository.findAll().size() > 0;
    }

    @Override
    public boolean removeSong(Long songId) {
        if(songRepository.existsById(songId)){
            songRepository.deleteById(songId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean songExistsByName(String songName) {
        return songRepository.existsSongByName(songName);
    }

    @Override
    public List<Song> findSongsByGenre(String genre) {
        return songRepository.findSongsByMusicGenre(genre);
    }
}
