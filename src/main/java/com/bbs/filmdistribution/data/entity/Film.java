package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table( name = "film" )
public class Film extends AbstractEntity
{
    private String name;

    private Integer length;

    @OneToOne
    private AgeGroup ageGroup;

    private Double price;

    @Transient
    private Integer availableCopies = 0;
}
