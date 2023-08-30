package com.bbs.filmdistribution.data.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Abstract database service with basic functions.
 */
public abstract class AbstractDatabaseService<T>
{

    /**
     * Delete an entity by id.
     *
     * @param id The id
     */
    public abstract void delete( Long id );

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
