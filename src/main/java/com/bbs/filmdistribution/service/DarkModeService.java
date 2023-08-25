package com.bbs.filmdistribution.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class DarkModeService
{

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

    public boolean isDarkModeActive()
    {
        return UI.getCurrent().getElement().getThemeList().contains( Lumo.DARK );
    }

}
