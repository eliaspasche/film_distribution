package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmCopy;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class FilmCopyService
{

    private final FilmCopyRepository repository;

    public FilmCopyService( FilmCopyRepository repository )
    {
        this.repository = repository;
    }

    public Optional<FilmCopy> get( Long id )
    {
        return repository.findById( id );
    }

    public FilmCopy update( FilmCopy entity )
    {
        return repository.save( entity );
    }

    public void delete( Long id )
    {
        repository.deleteById( id );
    }

    public Page<FilmCopy> list( Pageable pageable )
    {
        return repository.findAll( pageable );
    }

    public Page<FilmCopy> list( Pageable pageable, Specification<FilmCopy> filter )
    {
        return repository.findAll( filter, pageable );
    }

    public int count()
    {
        return ( int ) repository.count();
    }

}
