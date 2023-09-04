package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * This object represents a customer.
 */
@Entity
@Getter
@Setter
@Table( name = "customer" )
public class Customer extends AbstractEntity
{
    private String name;
    private String firstName;
    private LocalDate dateOfBirth;
    private String address;
    private String zipCode;
    private String city;
}
