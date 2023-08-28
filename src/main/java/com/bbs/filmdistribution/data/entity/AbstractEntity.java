package com.bbs.filmdistribution.data.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Override
    public int hashCode()
    {
        if ( getId() != null )
        {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( !( obj instanceof AbstractEntity that ) )
        {
            return false; // null or not an AbstractEntity class
        }
        if ( getId() != null )
        {
            return getId().equals( that.getId() );
        }
        return super.equals( that );
    }
}
