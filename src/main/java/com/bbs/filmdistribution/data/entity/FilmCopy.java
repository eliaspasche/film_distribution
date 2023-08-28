package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class FilmCopy extends AbstractEntity
{
    private UUID inventoryId;
    private Integer filmId;
}
