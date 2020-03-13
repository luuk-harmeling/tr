package com.rockstars.rockstar.rockstar.repository;

import com.rockstars.rockstar.rockstar.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    List<Song> findSongsByMusicGenre(String genre);

    boolean existsSongByName(String name);
}
