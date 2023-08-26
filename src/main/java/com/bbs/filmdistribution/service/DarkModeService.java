package com.bbs.filmdistribution.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Service to manage the dark/light theme on the application.
 */
@Component
public class DarkModeService
{

    /**
     * Change the current theme.
     * If light then dark and the other way around.
     *
     * @return An integer which represents the current theme (0=light, 1=dark)
     */
    public int changeDarkMode()
    {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();

        if ( themeList.contains( Lumo.DARK ) )
        {
            themeList.remove( Lumo.DARK );
            return 0;
        }
        else
        {
            themeList.add( Lumo.DARK );
            return 1;
        }
    }

    /**
     * Check if the application has the dark theme.
     *
     * @return Has application the dark theme.
     */
    public boolean isDarkModeActive()
    {
        return UI.getCurrent().getElement().getThemeList().contains( Lumo.DARK );
    }

}
