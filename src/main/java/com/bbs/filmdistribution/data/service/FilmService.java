package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.Film;
import com.bbs.filmdistribution.data.entity.FilmCopy;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for the {@link Film} object to make interactions with the database.
 */
@Service
public class FilmService extends AbstractDatabaseService<Film, FilmRepository>
{

    /**
     * The constructor.
     *
     * @param repository The {@link FilmRepository}
     */
    public FilmService( FilmRepository repository )
    {
        super( repository );
    }

    @Override
    public Film update( Film entity )
    {
        if ( entity.getId() == null )
        {
            for ( var i = 0; i < entity.getAvailableCopies(); i++ )
            {
                FilmCopy filmCopy = new FilmCopy();
                filmCopy.setFilm( entity );
                filmCopy.setInventoryNumber( UUID.randomUUID().toString() );

                entity.getFilmCopies().add( filmCopy );
            }
        }

        return getRepository().save( entity );
    }

    /**
     * Get the amount of available copies by an {@link Film} id
     *
     * @param id The id
     * @return The amount of available copies.
     */
    public int availableCopies( long id )
    {
        return getRepository().availableCopies( id );
    }
}
