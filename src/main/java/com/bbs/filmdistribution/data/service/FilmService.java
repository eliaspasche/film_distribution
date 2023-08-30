package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.Film;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService extends AbstractDatabaseService<Film>
{

    private final FilmRepository repository;


    public Optional<Film> get( Long id )
    {
        return repository.findById( id );
    }

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

    public int count()
    {
        return ( int ) repository.count();
    }

}
