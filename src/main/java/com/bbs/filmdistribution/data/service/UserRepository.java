package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>
{
    User findByUsername( String username );
}
