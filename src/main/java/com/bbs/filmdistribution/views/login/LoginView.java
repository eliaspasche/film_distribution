package com.bbs.filmdistribution.views.login;

import com.bbs.filmdistribution.config.AppConfig;
import com.bbs.filmdistribution.security.AuthenticatedUser;
import com.bbs.filmdistribution.views.dashboard.home.HomeView;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.*;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * Login view to authenticate a user with the application.
 */
@AnonymousAllowed
@PageTitle( "Login" )
@Route( "login" )
@RouteAlias( "" )
@RequiredArgsConstructor
public class LoginView extends LoginOverlay implements BeforeEnterObserver
{

    private final AuthenticatedUser authenticatedUser;
    private final AppConfig appConfig;

    /**
     * Initialization of the components in the current view.
     */
    @PostConstruct
    public void init()
    {
        setAction( RouteUtil.getRoutePath( VaadinService.getCurrent().getContext(), getClass() ) );

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader( new LoginI18n.Header() );
        i18n.getHeader().setTitle( "Film Distribution" );
        i18n.getHeader().setDescription( "Login using user/user or admin/admin" );
        i18n.setAdditionalInformation( this.appConfig.getVersion() );
        setI18n( i18n );

        setForgotPasswordButtonVisible( false );
        setOpened( true );
    }

    @Override
    public void beforeEnter( BeforeEnterEvent event )
    {
        if ( authenticatedUser.get().isPresent() )
        {
            // Already logged in
            setOpened( false );
            if ( LoginView.class.equals( event.getNavigationTarget() ) )
            {
                event.forwardTo( HomeView.class );
            }
            return;
        }

        setError( event.getLocation().getQueryParameters().getParameters().containsKey( "error" ) );
    }
}
