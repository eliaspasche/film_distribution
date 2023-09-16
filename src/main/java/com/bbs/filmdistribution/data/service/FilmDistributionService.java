package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.common.DistributionInvoiceDTO;
import com.bbs.filmdistribution.common.DistributionRevenueDTO;
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

    /**
     * Get a {@link List} with the top distributed films.
     *
     * @param amount The maximum value to be returned
     * @return The {@link List} of {@link TopFilmDistributionDTO}
     */
    public List<TopFilmDistributionDTO> getTopFilmDistributions( int amount )
    {
        return getRepository().getTopFilmDistributions( amount );
    }

    /**
     * Get a {@link List} with the distributed film revenue.
     *
     * @return The {@link List} of {@link DistributionRevenueDTO}
     */
    public List<DistributionRevenueDTO> getDistributionRevenue()
    {
        return getRepository().getDistributionRevenue();
    }

    /**
     * Get a {@link List} with the distributed films for a specific distribution to create invoice.
     *
     * @param distributionId The distribution id
     * @return The {@link List} of {@link DistributionInvoiceDTO}
     */
    public List<DistributionInvoiceDTO> getDistributionInvoiceByDistribution( long distributionId )
    {
        return getRepository().getDistributionInvoiceByDistribution( distributionId );
    }
}
