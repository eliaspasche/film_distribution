package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

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

    private Double price;

    @Transient
    private Integer availableCopies = 0;
}
