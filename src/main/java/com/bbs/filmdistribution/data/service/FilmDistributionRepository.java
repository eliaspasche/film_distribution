package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.common.TopFilmDistributionDTO;
import com.bbs.filmdistribution.data.entity.FilmDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The repository for the {@link FilmDistribution} object.
 */
public interface FilmDistributionRepository extends JpaRepository<FilmDistribution, Long>, JpaSpecificationExecutor<FilmDistribution>
{

    @Query( value = "select count(*) as currentDistributionAmount, f.NAME as filmName from FILM_DISTRIBUTION_ITEMS i join FILM_COPY c " +
            "on i.FILM_COPY_ID = c.ID join FILM f on c.FILM_ID = f.ID group by FILM_ID, f.name order by count(*) desc FETCH FIRST :amount ROWS ONLY", nativeQuery = true )
    List<TopFilmDistributionDTO> getTopFilmDistributions( int amount );

}
