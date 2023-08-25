package com.bbs.filmdistribution.security;

import com.bbs.filmdistribution.data.entity.User;
import com.bbs.filmdistribution.data.service.UserRepository;
import com.bbs.filmdistribution.service.SessionService;
import com.bbs.filmdistribution.util.NotificationUtil;
import com.vaadin.flow.spring.security.AuthenticationContext;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthenticatedUser
{

    private final String userSessionKey = User.class.getName();

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    private final SessionService sessionService;

    public AuthenticatedUser( AuthenticationContext authenticationContext, UserRepository userRepository, SessionService sessionService )
    {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
        this.sessionService = sessionService;
    }

    @Transactional
    public Optional<User> get()
    {
        User user = sessionService.getElementFromSession( userSessionKey );

        if ( user == null )
        {
            user = authenticationContext.getAuthenticatedUser( UserDetails.class )
                    .map( userDetails -> userRepository.findByUsername( userDetails.getUsername() ) )
                    .orElse( null );

            if ( user != null )
            {
                sessionService.setElementInSession( userSessionKey, user );
            }
        }

        return Optional.ofNullable( user );
    }

    public void logout()
    {
        sessionService.removeElementFromSession( userSessionKey );
        authenticationContext.logout();
    }

}
