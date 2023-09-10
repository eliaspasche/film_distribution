package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * The repository for the {@link Film} object.
 */
public interface FilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film>
{
    /**
     * Get the amount of available copies by an {@link Film} id
     *
     * @param id The id
     * @return The amount of available copies.
     */
    @Query( value = "select count(*) from Film_Copy where FILM_ID = :id", nativeQuery = true )
    int availableCopies( long id );
}
