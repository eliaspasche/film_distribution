package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
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
     * @param date {@link LocalDate}
     * @return The amount of distributions.
     */
    @Query(value = "SELECT COUNT(*) FROM film_distribution_items items LEFT JOIN film_distribution distribution ON items.film_distribution_id = distribution.id WHERE film_copy_id = :filmCopyId AND :date BETWEEN distribution.start_date AND distribution.end_date", nativeQuery = true)
    int getDistributionsByFilmCopyId(long filmCopyId, LocalDate date);

    /**
     * Get the available {@link FilmCopy} objects at a given date
     *
     * @param date {@link LocalDate}
     * @return The available {@link FilmCopy} as {@link List}
     */
    @Query(value = "SELECT copy.id, copy.inventory_number, copy.film_id FROM film_copy copy LEFT JOIN film_distribution_items items ON copy.id = items.film_copy_id LEFT JOIN film_distribution distribution ON items.film_distribution_id = distribution.id WHERE distribution.start_date IS NULL OR distribution.end_date IS NULL OR :date NOT BETWEEN distribution.start_date AND distribution.end_date", nativeQuery = true)
    List<FilmCopy> getAvailableCopies(LocalDate date);
}
