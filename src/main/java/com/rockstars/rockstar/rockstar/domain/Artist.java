package com.rockstars.rockstar.rockstar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;




@Getter
@Setter
@Entity
@Table(name = "artists")
public class Artist extends AbstractBaseEntity {


    @Column(unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "artist")
    @JsonIgnoreProperties(value = "artist", allowSetters = true)
    private List<Song> songs;

    public Artist() { }

    public Artist(String name) {
        this.name = name;
    }

}
