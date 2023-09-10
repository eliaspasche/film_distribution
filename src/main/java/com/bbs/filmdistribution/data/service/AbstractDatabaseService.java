package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.AbstractEntity;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Abstract database service with basic database functions.
 * @param <T> The {@link AbstractEntity}
 * @param <K> The {@link JpaRepository}
 */
@Getter
public abstract class AbstractDatabaseService<T extends AbstractEntity, K extends JpaRepository<T, Long> & JpaSpecificationExecutor<T>>
{

    
    private final K repository;

    /**
     * The constructor.
     *
     * @param repository The {@link JpaRepository}
     */
    protected AbstractDatabaseService( K repository )
    {
        this.repository = repository;
    }

    /**
     * Update an entity.
     *
     * @param entity The entity to update
     * @return The updated entity from database
     */
    public T update( T entity )
    {
        return repository.save( entity );
    }

    /**
     * Delete an entity by id.
     *
     * @param id The id
     */
    public void delete( Long id )
    {
        repository.deleteById( id );
    }

    /**
     * Get an entity by id.
     *
     * @param id The id
     * @return The entity
     */
    public Optional<T> get( Long id )
    {
        return repository.findById( id );
    }

    /**
     * Get a list of a specific entity (lazy loading).
     *
     * @param pageable The {@link Pageable} for lazy loading
     * @return The {@link Page} with entities
     */
    public Page<T> list( Pageable pageable )
    {
        return repository.findAll( pageable );
    }

    /**
     * Get a list of a specific entity (lazy loading).
     *
     * @param pageable The {@link Pageable} for lazy loading
     * @param filter   The {@link Specification} to filter in the database
     * @return The {@link Page} with entities
     */
    public Page<T> list( Pageable pageable, Specification<T> filter )
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
