package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmCopy;
import org.springframework.stereotype.Service;

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


}
