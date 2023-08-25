package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.Entity;

@Entity
public class AgeGroup extends AbstractEntity
{

    private Integer minimumAge;
    private String name;

    public Integer getMinimumAge()
    {
        return minimumAge;
    }

    public void setMinimumAge( Integer minimumAge )
    {
        this.minimumAge = minimumAge;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

}
