package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmDistribution;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmDistributionService
{

    private final FilmDistributionRepository repository;


    public Optional<FilmDistribution> get( Long id )
    {
        return repository.findById( id );
    }

    public FilmDistribution update( FilmDistribution entity )
    {
        return repository.save( entity );
    }

    public void delete( Long id )
    {
        repository.deleteById( id );
    }

    public Page<FilmDistribution> list( Pageable pageable )
    {
        return repository.findAll( pageable );
    }

    public Page<FilmDistribution> list( Pageable pageable, Specification<FilmDistribution> filter )
    {
        return repository.findAll( filter, pageable );
    }

    public int count()
    {
        return ( int ) repository.count();
    }

}
