package com.rockstars.rockstar.rockstar.domain.mapperObjects;


import lombok.Data;

@Data
public class SongDto {

    private String name;

    private String shortName;

    private int year;

    private String artist;

    private int bpm;

    private int duration;

    private String genre;

    private String spotifyId;

    private String album;
}
