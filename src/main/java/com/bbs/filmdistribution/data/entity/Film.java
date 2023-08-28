package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Film extends AbstractEntity
{
    private String name;
    private Integer length;
    private Integer ageGroupId;
    private Integer price;
    private Integer discount;
    private Integer availableCopies;
}
