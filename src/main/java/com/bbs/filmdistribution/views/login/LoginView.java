package com.bbs.filmdistribution.views.login;

import com.bbs.filmdistribution.security.AuthenticatedUser;
import com.bbs.filmdistribution.util.NotificationUtil;
import com.bbs.filmdistribution.views.dashboard.home.HomeView;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.*;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle( "Login" )
@Route( "login" )
@RouteAlias( "" )
public class LoginView extends LoginOverlay implements BeforeEnterObserver
{

    private final AuthenticatedUser authenticatedUser;

    public LoginView( AuthenticatedUser authenticatedUser )
    {
        this.authenticatedUser = authenticatedUser;
        setAction( RouteUtil.getRoutePath( VaadinService.getCurrent().getContext(), getClass() ) );

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader( new LoginI18n.Header() );
        i18n.getHeader().setTitle( "Film Distribution" );
        i18n.getHeader().setDescription( "Login using user/user or admin/admin" );
        i18n.setAdditionalInformation( null );
        setI18n( i18n );

        setForgotPasswordButtonVisible( false );
        setOpened( true );

        addLoginListener( loginEvent -> NotificationUtil.sendLoginNotification() );
    }

    @Override
    public void beforeEnter( BeforeEnterEvent event )
    {
        if ( authenticatedUser.get().isPresent() )
        {
            // Already logged in
            setOpened( false );
            event.forwardTo( HomeView.class );
            return;
        }

        setError( event.getLocation().getQueryParameters().getParameters().containsKey( "error" ) );
    }
}
