package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Customer extends AbstractEntity
{
    private String name;
    private String firstName;
    private LocalDate dateOfBirth;
    private String address;
    private String zipCode;
    private String city;
}
