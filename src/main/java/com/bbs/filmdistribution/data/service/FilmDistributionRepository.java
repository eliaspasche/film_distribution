package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.common.DistributionRevenueDTO;
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

    /**
     * Get a {@link List} with the top distributed films.
     *
     * @param amount The maximum value to be returned
     * @return The {@link List} of {@link TopFilmDistributionDTO}
     */
    @Query( value = "select count(*) as currentDistributionAmount, f.NAME as filmName from FILM_DISTRIBUTION_ITEMS i join FILM_COPY c " +
            "on i.FILM_COPY_ID = c.ID join FILM f on c.FILM_ID = f.ID group by FILM_ID, f.name order by count(*) desc FETCH FIRST :amount ROWS ONLY", nativeQuery = true )
    List<TopFilmDistributionDTO> getTopFilmDistributions( int amount );

    /**
     * Get a {@link List} with the distributed film revenue.
     *
     * @return The {@link List} of {@link DistributionRevenueDTO}
     */
    @Query( value = "select f.name as filmName, cast(sum((f.PRICE / 7) * trunc((d.END_DATE - d.START_DATE))) AS DECIMAL(5, 2)) as revenue " +
            "from FILM_DISTRIBUTION_ITEMS i join FILM_DISTRIBUTION d on d.ID = i.FILM_DISTRIBUTION_ID join FILM_COPY c on c.ID = i.FILM_COPY_ID " +
            "join FILM f on f.ID = c.FILM_ID group by f.name order by revenue desc", nativeQuery = true )
    List<DistributionRevenueDTO> getDistributionRevenue();
}
