package com.bbs.filmdistribution.util;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class to create items for a {@link com.vaadin.flow.component.menubar.MenuBar}.
 */
@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class MenuBarUtil
{
    public static MenuItem createIconItem( HasMenuItems menu, VaadinIcon iconName, String toolTipText,
                                           String label )
    {
        return createIconItem( menu, iconName, toolTipText, label, null, false );
    }

    public static MenuItem createIconItem( HasMenuItems menu, VaadinIcon iconName, String toolTipText,
                                           String label, String iconColor )
    {
        return createIconItem( menu, iconName, toolTipText, label, iconColor, false );
    }

    public static MenuItem createIconItem( HasMenuItems menu, VaadinIcon iconName, String toolTipText,
                                           String label, String iconColor, boolean isChild )
    {
        Icon icon = new Icon( iconName );
        icon.setTooltipText( toolTipText != null ? toolTipText : "" );
        if ( iconColor != null )
        {
            icon.getStyle().set( "color", iconColor );
        }

        if ( isChild )
        {
            icon.getStyle().set( "width", "var(--lumo-icon-size-s)" );
            icon.getStyle().set( "height", "var(--lumo-icon-size-s)" );
            icon.getStyle().set( "marginRight", "var(--lumo-space-s)" );
        }

        MenuItem item = menu.addItem( icon, e -> {
        } );
        if ( label != null )
        {
            item.add( new Text( label ) );
        }

        return item;
    }
}
