package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.Film;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for the {@link Film} object to make interactions with the database.
 */
@Service
@RequiredArgsConstructor
public class FilmService extends AbstractDatabaseService<Film>
{

    private final FilmRepository repository;

    @Override
    public Optional<Film> get( Long id )
    {
        return repository.findById( id );
    }

    @Override
    public Film update( Film entity )
    {
        return repository.save( entity );
    }

    @Override
    public void delete( Long id )
    {
        repository.deleteById( id );
    }

    @Override
    public Page<Film> list( Pageable pageable )
    {
        return repository.findAll( pageable );
    }

    @Override
    public Page<Film> list( Pageable pageable, Specification<Film> filter )
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

    /**
     * Get the amount of available copies by an {@link Film} id
     *
     * @param id The id
     * @return The amount of available copies.
     */
    public int availableCopies( long id )
    {
        return repository.availableCopies( id );
    }
}
