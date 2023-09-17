package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This object represents a film.
 */
@Entity
@Getter
@Setter
@Table( name = "film" )
public class Film extends AbstractEntity
{
    private String name;
    private Integer length;

    @ManyToOne
    private AgeGroup ageGroup;

    @OneToMany( mappedBy = "film", cascade = CascadeType.ALL )
    private List<FilmCopy> filmCopies = new ArrayList<>();

    private Double price;

    @Transient
    private Integer availableCopies = 0;
}
