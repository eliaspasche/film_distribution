package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
public class FilmCopy extends AbstractEntity
{

    private UUID inventoryId;
    private Integer filmId;

    public UUID getInventoryId()
    {
        return inventoryId;
    }

    public void setInventoryId( UUID inventoryId )
    {
        this.inventoryId = inventoryId;
    }

    public Integer getFilmId()
    {
        return filmId;
    }

    public void setFilmId( Integer filmId )
    {
        this.filmId = filmId;
    }

}
