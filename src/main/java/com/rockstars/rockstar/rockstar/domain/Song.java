package com.rockstars.rockstar.rockstar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "songs")
public class Song extends AbstractBaseEntity{

    private String name;

    private String shortName;

    private int year;


    @ManyToOne
    @JoinColumn(name = "fk_artist_id", nullable = false)
    @JsonIgnoreProperties(value = "songs", allowSetters = true)
    private Artist artist;

    private int bpm;

    private int duration;

    private String musicGenre;

    private String spotifyId;

    private String album;

}







