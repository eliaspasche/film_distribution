package com.bbs.filmdistribution.data.service;

/**
 * Abstract database service with basic functions.
 */
public abstract class AbstractDatabaseService
{

    /**
     * Delete an entity by id.
     *
     * @param id The id
     */
    public abstract void delete( Long id );

}
