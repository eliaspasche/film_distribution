package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.Entity;

@Entity
public class Film extends AbstractEntity
{

    private String name;
    private Integer length;
    private Integer ageGroupId;
    private Integer price;
    private Integer discount;
    private Integer availableCopies;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Integer getLength()
    {
        return length;
    }

    public void setLength( Integer length )
    {
        this.length = length;
    }

    public Integer getAgeGroupId()
    {
        return ageGroupId;
    }

    public void setAgeGroupId( Integer ageGroupId )
    {
        this.ageGroupId = ageGroupId;
    }

    public Integer getPrice()
    {
        return price;
    }

    public void setPrice( Integer price )
    {
        this.price = price;
    }

    public Integer getDiscount()
    {
        return discount;
    }

    public void setDiscount( Integer discount )
    {
        this.discount = discount;
    }

    public Integer getAvailableCopies()
    {
        return availableCopies;
    }

    public void setAvailableCopies( Integer availableCopies )
    {
        this.availableCopies = availableCopies;
    }

}
