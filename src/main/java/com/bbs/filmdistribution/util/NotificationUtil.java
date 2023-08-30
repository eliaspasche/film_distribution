package com.bbs.filmdistribution.util;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * Utility class to create notification for the application
 */
public class NotificationUtil
{

    /**
     * The constructor
     */
    private NotificationUtil()
    {
        // Private constructor to hide the implicit public one
    }

    /**
     * Create a notification with a text, duration in seconds and a position.
     *
     * @param text              The text
     * @param durationInSeconds The duration in seconds
     * @param position          The {@link Notification.Position}
     * @param variant           The {@link NotificationVariant}
     */
    private static void sendNotification( String text, int durationInSeconds, Notification.Position position, NotificationVariant variant )
    {
        Notification notification = Notification.show( text, durationInSeconds * 1000, position );
        if ( variant != null )
        {
            notification.addThemeVariants( variant );
        }
    }

    /**
     * Show a login notification
     */
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

    /**
     * Create and show a success notification with defined text and a duration value in seconds.
     *
     * @param text              The text do display
     * @param durationInSeconds The duration in seconds
     */
    public static void sendSuccessNotification( String text, int durationInSeconds )
    {
        sendNotification( text, durationInSeconds, Notification.Position.BOTTOM_END, NotificationVariant.LUMO_SUCCESS );
    }

    /**
     * Create and show a error notification with defined text and a duration value in seconds.
     *
     * @param text              The text do display
     * @param durationInSeconds The duration in seconds
     */
    public static void sendErrorNotification( String text, int durationInSeconds )
    {
        sendNotification( text, durationInSeconds, Notification.Position.BOTTOM_END, NotificationVariant.LUMO_ERROR );
    }

}
