package com.bbs.filmdistribution.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @Enumerated( EnumType.STRING )
    private UserRole userRole;
}
