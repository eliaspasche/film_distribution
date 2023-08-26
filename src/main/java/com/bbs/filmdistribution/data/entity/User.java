package com.bbs.filmdistribution.data.entity;

import com.bbs.filmdistribution.data.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
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

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getHashedPassword()
    {
        return hashedPassword;
    }

    public void setHashedPassword( String hashedPassword )
    {
        this.hashedPassword = hashedPassword;
    }

    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles( Set<Role> roles )
    {
        this.roles = roles;
    }

    public int getDarkMode()
    {
        return darkMode;
    }

    public void setDarkMode( int darkMode )
    {
        this.darkMode = darkMode;
    }
}
