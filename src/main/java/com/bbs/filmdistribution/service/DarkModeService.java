package com.bbs.filmdistribution.service;

import com.bbs.filmdistribution.event.ThemeVariantChangedEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.stereotype.Component;

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

        String currentTheme = Lumo.LIGHT;
        if ( isDarkModeActive() )
        {
            themeList.remove( Lumo.DARK );
        }
        else
        {
            themeList.add( Lumo.DARK );
            currentTheme = Lumo.DARK;
        }

        UI ui = UI.getCurrent();
        ComponentUtil.fireEvent( ui, new ThemeVariantChangedEvent( ui, currentTheme ) );
        return isDarkModeActive() ? 1 : 0;
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
