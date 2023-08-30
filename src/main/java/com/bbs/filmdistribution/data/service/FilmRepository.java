package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film>
{
    @Query( value = "select count(*) from Film_Copy c where c.film_id = :id", nativeQuery = true )
    int availableCopies( long id );
}
