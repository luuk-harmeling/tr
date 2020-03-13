package com.rockstars.rockstar.rockstar.repository;

import com.rockstars.rockstar.rockstar.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Artist findArtistByName(String name);

    boolean existsArtistByName(String name);


}
