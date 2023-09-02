package com.bbs.filmdistribution.data.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

/**
 * Abstract database service with basic database functions.
 * @param <T> The defined entity
 */
public abstract class AbstractDatabaseService<T>
{

    /**
     * Update an entity.
     *
     * @param entity The entity to update
     * @return The updated entity from database
     */
    public abstract T update( T entity );

    /**
     * Delete an entity by id.
     *
     * @param id The id
     */
    public abstract void delete( Long id );

    /**
     * Get an entity by id.
     *
     * @param id The id
     * @return The entity
     */
    public abstract Optional<T> get( Long id );

    /**
     * Get a list of a specific entity (lazy loading).
     *
     * @param pageable The {@link Pageable} for lazy loading
     * @return The {@link Page} with entities
     */
    public abstract Page<T> list( Pageable pageable );

    /**
     * Get a list of a specific entity (lazy loading).
     *
     * @param pageable The {@link Pageable} for lazy loading
     * @param filter   The {@link Specification} to filter in the database
     * @return The {@link Page} with entities
     */
    public abstract Page<T> list( Pageable pageable, Specification<T> filter );


}
