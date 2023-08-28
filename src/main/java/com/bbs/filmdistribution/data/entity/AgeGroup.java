package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table( name = "age_group" )
public class AgeGroup extends AbstractEntity
{
    private Integer minimumAge;
    private String name;

    @OneToMany( mappedBy = "ageGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private List<Film> films;
}
