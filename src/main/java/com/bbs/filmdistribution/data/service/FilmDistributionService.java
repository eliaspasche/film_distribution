package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmDistribution;
import org.springframework.stereotype.Service;

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

    public int count()
    {
        return ( int ) getRepository().count();
    }

}
