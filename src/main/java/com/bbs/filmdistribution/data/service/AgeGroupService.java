package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.AgeGroup;
import org.springframework.stereotype.Service;

/**
 * Service for the {@link AgeGroup} object to make interactions with the database.
 */
@Service
public class AgeGroupService extends AbstractDatabaseService<AgeGroup, AgeGroupRepository>
{

    /**
     * The constructor.
     *
     * @param repository The {@link AgeGroupRepository}
     */
    public AgeGroupService( AgeGroupRepository repository )
    {
        super( repository );
    }

}
