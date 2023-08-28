package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table( name = "film_copy" )
public class FilmCopy extends AbstractEntity
{
    private String inventoryNumber;

    @ManyToOne( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private Film film;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "film_distribution_items", joinColumns = @JoinColumn( name = "film_copy_id" ), inverseJoinColumns = @JoinColumn( name = "film_distribution_id" ) )
    private List<FilmDistribution> filmDistributions;
}
