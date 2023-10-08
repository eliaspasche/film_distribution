package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmCopy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for the {@link FilmCopy} object to make interactions with the database.
 */
@Service
public class FilmCopyService extends AbstractDatabaseService<FilmCopy, FilmCopyRepository>
{

    /**
     * Constructor.
     *
     * @param repository The {@link FilmCopyRepository}
     */
    public FilmCopyService( FilmCopyRepository repository )
    {
        super( repository );
    }

    /**
     * Check if the {@link FilmCopy} object is available.
     *
     * @param id   The id of the {@link FilmCopy}
     * @param date {@link LocalDate}
     * @return object is available
     */
    public boolean isFilmCopyAvailable( long id, LocalDate date )
    {
        return getRepository().getDistributionsByFilmCopyId( id, date ) == 0;
    }

    /**
     * Get the available {@link FilmCopy} objects at a given date
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return The available {@link FilmCopy} as {@link List}
     */
    public List<FilmCopy> getAvailableCopies( LocalDate startDate, LocalDate endDate )
    {
        return getRepository().getAvailableCopies( startDate, endDate );
    }

}
