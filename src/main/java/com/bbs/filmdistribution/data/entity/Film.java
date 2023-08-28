package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.*;
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

    @ManyToOne( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private AgeGroup ageGroup;

    private Integer price;

    @Transient
    private Integer availableCopies = 0;
}
