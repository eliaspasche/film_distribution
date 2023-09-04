package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FilmDistributionRepository extends JpaRepository<FilmDistribution, Long>, JpaSpecificationExecutor<FilmDistribution>
{
    @Query( value = "select FILM_ID, count(*) as amount from FILM_COPY c group by c.FILM_ID having c.FILM_ID = :id AND amount < (\n" +
            "    select count(*) from FILM_DISTRIBUTION_ITEMS i where i.film_copy_id = :id)", nativeQuery = true )
    int isCopyAvailable( long id );
}
