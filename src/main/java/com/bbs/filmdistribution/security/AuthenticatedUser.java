package com.bbs.filmdistribution.security;

import com.bbs.filmdistribution.config.AppConfig;
import com.bbs.filmdistribution.data.entity.User;
import com.bbs.filmdistribution.data.service.UserRepository;
import com.bbs.filmdistribution.service.CookieService;
import com.bbs.filmdistribution.service.SessionService;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class AuthenticatedUser
{

    private final String userSessionKey = User.class.getName();

    private final AppConfig appConfig;
    private final CookieService cookieService;
    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;
    private final SessionService sessionService;

    public AuthenticatedUser( AppConfig appConfig, CookieService cookieService, AuthenticationContext authenticationContext,
                              UserRepository userRepository, SessionService sessionService )
    {
        this.appConfig = appConfig;
        this.cookieService = cookieService;
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
                if ( appConfig.isAutoLogin() )
                {
                    cookieService.setJSCookie( AppConfig.AUTO_LOGIN_KEY, user.getUsername(), 1 );
                    System.out.println( "Added autologin cookie" );
                }
            }
        }

        return Optional.ofNullable( user );
    }

    public void logout()
    {
        cookieService.removeJSCookie( AppConfig.AUTO_LOGIN_KEY );
        sessionService.removeElementFromSession( userSessionKey );
        authenticationContext.logout();
    }

}
