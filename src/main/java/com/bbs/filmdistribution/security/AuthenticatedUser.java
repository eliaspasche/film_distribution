package com.bbs.filmdistribution.security;

import com.bbs.filmdistribution.config.AppConfig;
import com.bbs.filmdistribution.data.entity.User;
import com.bbs.filmdistribution.data.service.UserRepository;
import com.bbs.filmdistribution.service.CookieService;
import com.bbs.filmdistribution.service.SessionService;
import com.bbs.filmdistribution.util.NotificationUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service to handle the logged-in user in the session.
 */
@Component
@RequiredArgsConstructor
public class AuthenticatedUser
{
    private final String userSessionKey = User.class.getName();

    private final AppConfig appConfig;
    private final CookieService cookieService;
    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;
    private final SessionService sessionService;

    /**
     * Put the {@link User} in the current session if the credentials are valid.
     * If the user already logged in, get the {@link User} from session.
     *
     * @return The {@link User}
     */
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
                }
                UI.getCurrent().access( NotificationUtil::sendLoginNotification );
            }
        }

        return Optional.ofNullable( user );
    }

    /**
     * Remove the {@link User} from the current session.
     */
    public void logout()
    {
        cookieService.removeJSCookie( AppConfig.AUTO_LOGIN_KEY );
        sessionService.removeElementFromSession( userSessionKey );
        authenticationContext.logout();
    }

}
