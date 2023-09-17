package com.bbs.filmdistribution.security;

import com.bbs.filmdistribution.data.entity.User;
import com.bbs.filmdistribution.data.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Implementation for the {@link UserDetails} to handle the login for a {@link User}
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService
{

    private final UserRepository userRepository;

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
            return new org.springframework.security.core.userdetails.User( user.getUsername(), user.getHashedPassword(), Collections.singletonList( new SimpleGrantedAuthority( "ROLE_" + user.getUserRole() ) ) );
        }
    }

}
