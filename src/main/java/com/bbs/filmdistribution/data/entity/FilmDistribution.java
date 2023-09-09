package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table( name = "film_distribution" )
public class FilmDistribution extends AbstractEntity
{
    @ManyToOne
    private Customer customer;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "film_distribution_items", joinColumns = @JoinColumn( name = "film_distribution_id" ), inverseJoinColumns = @JoinColumn( name = "film_copy_id" ) )
    private List<FilmCopy> filmCopies;

    private LocalDate startDate;

    private LocalDate endDate;
}
