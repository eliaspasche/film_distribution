package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmCopy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for the {@link FilmCopy} object to make interactions with the database.
 */
@Service
public class FilmCopyService extends AbstractDatabaseService<FilmCopy, FilmCopyRepository>
{

    /**
     * The constructor.
     *
     * @param repository The {@link FilmCopyRepository}
     */
    public FilmCopyService( FilmCopyRepository repository )
    {
        super( repository );
    }

    public int count()
    {
        return ( int ) getRepository().count();
    }


    /**
     * Check if the {@link FilmCopy} object is available.
     *
     * @param id The id of the {@link FilmCopy}
     * @return object is available
     */
    public boolean isFilmCopyAvailable( long id )
    {
        return getRepository().getDistributionsByFilmCopyId( id ) == 0;
    }

    /**
     * Get the available {@link FilmCopy} objects
     *
     * @return The available {@link FilmCopy} as {@link List}
     */
    public List<FilmCopy> getAvailableCopies()
    {
        return getRepository().getAvailableCopies();
    }

}
