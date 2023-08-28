package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AgeGroup extends AbstractEntity
{
    private Integer minimumAge;
    private String name;
}
