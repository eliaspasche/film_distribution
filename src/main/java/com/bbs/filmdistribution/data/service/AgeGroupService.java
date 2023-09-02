package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.AgeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for the {@link AgeGroup} object to make interactions with the database.
 */
@Service
@RequiredArgsConstructor
public class AgeGroupService extends AbstractDatabaseService<AgeGroup>
{

    private final AgeGroupRepository repository;

    @Override
    public Optional<AgeGroup> get( Long id )
    {
        return repository.findById( id );
    }

    @Override
    public AgeGroup update( AgeGroup entity )
    {
        return repository.save( entity );
    }

    @Override
    public void delete( Long id )
    {
        repository.deleteById( id );
    }

    @Override
    public Page<AgeGroup> list( Pageable pageable )
    {
        return repository.findAll( pageable );
    }

    @Override
    public Page<AgeGroup> list( Pageable pageable, Specification<AgeGroup> filter )
    {
        return repository.findAll( filter, pageable );
    }

    /**
     * Get the amount of entities in the database.
     *
     * @return The amount of entities
     */
    public int count()
    {
        return ( int ) repository.count();
    }

}
