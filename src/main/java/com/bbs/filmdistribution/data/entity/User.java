package com.bbs.filmdistribution.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

/**
 * This object represents the logged-in user.
 */
@Entity
@Getter
@Setter
@Table( name = "application_user" )
public class User extends AbstractEntity
{
    private String username;

    private String name;

    @JsonIgnore
    private String hashedPassword;

    @Transient
    private int darkMode;
}
