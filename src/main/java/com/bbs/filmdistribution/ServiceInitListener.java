package com.bbs.filmdistribution;

import com.bbs.filmdistribution.config.AppConfig;
import com.bbs.filmdistribution.service.CookieService;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServiceInitListener;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * UI initialize listener.
 * Handle new sessions and ui events.
 */
@Component
@RequiredArgsConstructor
public class ServiceInitListener implements VaadinServiceInitListener
{

    private static final Logger LOGGER = Logger.getLogger( ServiceInitListener.class.getName() );

    private final AppConfig appConfig;
    private final UserDetailsService userDetailsService;
    private final CookieService cookieService;

    @Override
    public void serviceInit( ServiceInitEvent event )
    {
        event.getSource().addSessionInitListener( sessionInitEvent -> autoLogin( sessionInitEvent.getRequest() ) );
    }

    /**
     * Handle the auto login.
     * Check the defined key and set the required values in the session.
     *
     * @param vaadinRequest The {@link VaadinRequest}
     */
    private void autoLogin( VaadinRequest vaadinRequest )
    {
        String autoLoginUsername = cookieService.getJSCookie( AppConfig.AUTO_LOGIN_KEY );
        if ( autoLoginUsername == null )
        {
            return;
        }

        if ( !appConfig.isAutoLogin() )
        {
            cookieService.removeJSCookie( AppConfig.AUTO_LOGIN_KEY );
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername( autoLoginUsername );

        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken( userDetails, userDetails.getPassword(), userDetails.getAuthorities() );
        authentication.setDetails( new WebAuthenticationDetails( vaadinRequest.getRemoteAddr(), vaadinRequest.getWrappedSession().getId() ) );

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication( authentication );

        vaadinRequest.getWrappedSession().setAttribute( SPRING_SECURITY_CONTEXT_KEY, securityContext );

        LOGGER.info( "Auto login: " + autoLoginUsername );
    }

}
