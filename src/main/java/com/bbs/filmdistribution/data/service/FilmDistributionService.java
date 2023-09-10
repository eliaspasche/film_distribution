package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.common.TopFilmDistributionDTO;
import com.bbs.filmdistribution.data.entity.FilmDistribution;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for the {@link FilmDistribution} object to make interactions with the database.
 */
@Service
public class FilmDistributionService extends AbstractDatabaseService<FilmDistribution, FilmDistributionRepository>
{

    /**
     * The constructor.
     *
     * @param filmDistributionRepository The {@link FilmDistributionRepository}
     */
    public FilmDistributionService( FilmDistributionRepository filmDistributionRepository )
    {
        super( filmDistributionRepository );
    }

    public List<TopFilmDistributionDTO> getTopFilmDistributions( int amount )
    {
        return getRepository().getTopFilmDistributions( amount );
    }

}
