package com.bbs.filmdistribution.service;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;

/**
 * Service to manage values in the {@link VaadinSession}.
 */
@Component
public class SessionService
{

    /**
     * Set an object with a defined key in the current {@link VaadinSession}
     *
     * @param key The key
     * @param obj The object to set
     * @param <V> The type of the object
     * @return Object is successfully placed
     */
    public <V> boolean setElementInSession( String key, V obj )
    {
        VaadinSession.getCurrent().setAttribute( key, obj );

        return VaadinSession.getCurrent().getAttribute( key ) != null;
    }

    /**
     * Remove a current object in the {@link VaadinSession} by the defined key.
     *
     * @param key The key.
     * @return Object is successfully removed
     */
    public boolean removeElementFromSession( String key )
    {
        if ( VaadinSession.getCurrent().getAttribute( key ) != null )
        {
            VaadinSession.getCurrent().setAttribute( key, null );
        }

        return VaadinSession.getCurrent().getAttribute( key ) == null;
    }

    /**
     * Get an object by the defined key from a {@link VaadinSession}
     *
     * @param key The key
     * @return The object
     * @param <V> The type of the object
     */
    @SuppressWarnings( "unchecked" )
    public <V> V getElementFromSession( String key )
    {
        return VaadinSession.getCurrent() != null ? ( V ) VaadinSession.getCurrent().getAttribute( key ) : null;
    }

}
