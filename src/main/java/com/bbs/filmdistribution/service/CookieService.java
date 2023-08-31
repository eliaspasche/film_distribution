package com.bbs.filmdistribution.service;

import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Service to manage browser cookies.
 */
@Component
public class CookieService
{

    /**
     * Get a {@link Cookie} list from {@link VaadinService}
     * @return
     */
    private Cookie[] getCookies()
    {
        return VaadinService.getCurrentRequest().getCookies();
    }

    /**
     * Set a cookie with key, value and expiry amount in days.
     *
     * @param key The cookie key
     * @param value The cookie value
     * @param timeInDays The expiry in days
     */
    public void setJSCookie( String key, String value, int timeInDays )
    {
        int daySeconds = 86_400;
        int rejectTime = ( daySeconds * timeInDays );

        Cookie cookie = new Cookie( key, value );
        cookie.setMaxAge( rejectTime );
        cookie.setPath( "/" );

        VaadinService.getCurrentResponse().addCookie( cookie );
    }

    /**
     * Get a value from an {@link Cookie} from the Browser.
     *
     * @param key The cookie key
     * @return The value of the cookie (Or null if not exists)
     */
    public String getJSCookie( String key )
    {

        Cookie[] cookies = getCookies();
        if ( cookies == null )
            return null;

        Cookie cookie = Arrays.stream( cookies ).filter( cookieInList -> cookieInList.getName().equals( key ) )
                .findAny().orElse( null );

        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * Remove a {@link Cookie} by key
     *
     * @param key The cookie key
     */
    public void removeJSCookie( String key )
    {
        Cookie[] cookies = getCookies();
        if ( cookies == null )
            return;

        Cookie cookie = Arrays.stream( cookies ).filter( cookieInList -> cookieInList.getName().equals( key ) )
                .findAny().orElse( null );

        if ( cookie == null )
            return;

        cookie.setMaxAge( 0 ); // 0 = delete cookie
        VaadinService.getCurrentResponse().addCookie( cookie );
    }


}
