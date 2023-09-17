package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The repository for the {@link FilmCopy} object.
 */
public interface FilmCopyRepository extends JpaRepository<FilmCopy, Long>, JpaSpecificationExecutor<FilmCopy>
{
    /**
     * Get the amount of distributions by a {@link FilmCopy}
     *
     * @param filmCopyId The id of a {@link FilmCopy}
     * @return The amount of distributions.
     */
    @Query( value = "select count(*) from FILM_DISTRIBUTION_ITEMS where FILM_COPY_ID = :filmCopyId", nativeQuery = true )
    int getDistributionsByFilmCopyId( long filmCopyId );

    /**
     * Get the available {@link FilmCopy} objects
     *
     * @return The available {@link FilmCopy} as {@link List}
     */
    @Query( value = "select * from FILM_COPY where ID NOT IN (select FILM_COPY_ID from FILM_DISTRIBUTION_ITEMS)", nativeQuery = true )
    List<FilmCopy> getAvailableCopies();
}
