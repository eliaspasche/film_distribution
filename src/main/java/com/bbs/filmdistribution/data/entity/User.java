package com.bbs.filmdistribution.data.entity;

import com.bbs.filmdistribution.data.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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
    @Enumerated( EnumType.STRING )
    @ElementCollection( fetch = FetchType.EAGER )
    private Set<Role> roles;

    @Transient
    private int darkMode;
}
