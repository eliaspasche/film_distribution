package com.bbs.filmdistribution.service;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;

@Component
public class SessionService
{

    /* Element Session Options (Transfer over Views) */
    /* <V> Generische Methode */
    public <V> boolean setElementInSession( String key, V obj )
    {
        VaadinSession.getCurrent().setAttribute( key, obj );

        return VaadinSession.getCurrent().getAttribute( key ) != null;
    }

    public boolean removeElementFromSession( String key )
    {
        if ( VaadinSession.getCurrent().getAttribute( key ) != null )
        {
            VaadinSession.getCurrent().setAttribute( key, null );
        }

        return VaadinSession.getCurrent().getAttribute( key ) == null;
    }

    @SuppressWarnings( "unchecked" )
    public <V> V getElementFromSession( String key )
    {
        if ( VaadinSession.getCurrent() == null )
            return null;

        return ( V ) VaadinSession.getCurrent().getAttribute( key );
    }

}
