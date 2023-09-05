package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The repository for the {@link User} object.
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>
{
    /**
     * Find a {@link User} by username.
     *
     * @param username The username
     * @return The {@link User}
     */
    User findByUsername( String username );
}
