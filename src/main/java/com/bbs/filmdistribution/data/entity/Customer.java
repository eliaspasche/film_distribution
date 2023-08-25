package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Customer extends AbstractEntity
{

    private String name;
    private String firstName;
    private LocalDate dateOfBirth;
    private String address;
    private String zipCode;
    private String city;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    public LocalDate getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth( LocalDate dateOfBirth )
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress( String address )
    {
        this.address = address;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode( String zipCode )
    {
        this.zipCode = zipCode;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity( String city )
    {
        this.city = city;
    }

}
