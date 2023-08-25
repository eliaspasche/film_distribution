package com.bbs.filmdistribution.util;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class NotificationUtil
{

    private static void sendNotification( String text, int durationInSeconds, Notification.Position position )
    {
        Notification.show( text, 5000, position );
    }

    public static void sendLoginNotification()
    {
        Notification notification = new Notification();
        notification.setPosition( Notification.Position.BOTTOM_END );
        notification.setDuration( 2_000 );
        notification.addThemeVariants( NotificationVariant.LUMO_SUCCESS );

        Icon icon = VaadinIcon.USER.create();
        Div info = new Div( new Text( "Erfolgreich eingeloggt" ) );

        HorizontalLayout layout = new HorizontalLayout( icon, info );

        notification.add( layout );
        notification.open();
    }

    public static void sendSuccessNotification( String text, int durationInSeconds )
    {
        sendNotification( text, durationInSeconds, Notification.Position.BOTTOM_END );
    }

}
