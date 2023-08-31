package com.bbs.filmdistribution;

import com.bbs.filmdistribution.config.AppConfig;
import com.bbs.filmdistribution.security.UserDetailsServiceImpl;
import com.bbs.filmdistribution.service.CookieService;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.jboss.logging.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * UI initialize listener.
 * Handle new sessions and ui events.
 */
@Component
public class ServiceInitListener implements VaadinServiceInitListener
{

    private static final Logger LOGGER = Logger.getLogger( ServiceInitListener.class.getName());

    private final AppConfig appConfig;
    private final UserDetailsService userDetailsService;
    private final CookieService cookieService;

    /**
     * The constructor.
     *
     * @param appConfig The {@link AppConfig}
     * @param cookieService The {@link CookieService}
     * @param userDetailsService The {@link UserDetailsService}
     */
    public ServiceInitListener( AppConfig appConfig, CookieService cookieService,
                                UserDetailsServiceImpl userDetailsService )
    {
        this.appConfig = appConfig;
        this.cookieService = cookieService;
        this.userDetailsService = userDetailsService;

    }

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
            if ( !appConfig.isAutoLogin() )
            {
                cookieService.removeJSCookie( AppConfig.AUTO_LOGIN_KEY );
            }
            return;
        }

        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken( userDetailsService.loadUserByUsername( autoLoginUsername ), "user", Collections.emptyList() );
        authentication.setDetails( new WebAuthenticationDetails( vaadinRequest.getRemoteAddr(), vaadinRequest.getWrappedSession().getId() ) );

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication( authentication );

        vaadinRequest.getWrappedSession().setAttribute( SPRING_SECURITY_CONTEXT_KEY, securityContext );

        LOGGER.info( "Auto login: " + autoLoginUsername );
    }

}
