package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.User;
import org.springframework.stereotype.Service;

/**
 * Service for the {@link User} object to make interactions with the database.
 */
@Service
public class UserService extends AbstractDatabaseService<User, UserRepository>
{

    /**
     * Constructor.
     *
     * @param repository The {@link UserRepository}
     */
    public UserService( UserRepository repository )
    {
        super( repository );
    }

}
