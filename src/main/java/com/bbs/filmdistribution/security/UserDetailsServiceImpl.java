package com.bbs.filmdistribution.security;

import com.bbs.filmdistribution.data.entity.User;
import com.bbs.filmdistribution.data.service.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

    private final UserRepository userRepository;

    public UserDetailsServiceImpl( UserRepository userRepository )
    {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername( username );
        if ( user == null )
        {
            throw new UsernameNotFoundException( "No user present with username: " + username );
        }
        else
        {
            return new org.springframework.security.core.userdetails.User( user.getUsername(), user.getHashedPassword(), Collections.emptyList() );
        }
    }

}
