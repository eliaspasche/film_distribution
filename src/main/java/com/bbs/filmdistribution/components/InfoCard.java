package com.bbs.filmdistribution.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Info card component
 * Base: frontend/src/litelements/infocard.js
 */
@Tag( "info-card" )
@JsModule( "./src/litelements/infocard.js" )
public class InfoCard extends LitTemplate
{

    private ScheduledExecutorService executor;

    /**
     * The constructor.
     *
     * @param title       The title
     * @param description The description
     * @param withEffect  With counter effect
     */
    public InfoCard( String title, String description, boolean withEffect )
    {
        getElement().setProperty( "desc", description );
        if ( withEffect )
        {
            countToNumber( title );
        }
        getElement().setProperty( "title", title );
    }

    /**
     * The constructor.
     *
     * @param title       The title
     * @param description The description
     */
    public InfoCard( String title, String description )
    {
        this( title, description, false );
    }

    private int current;

    /**
     * Display the counter effect to the component.
     *
     * @param title The title.
     */
    private void countToNumber( String title )
    {
        int goal;
        try
        {
            goal = Integer.parseInt( title );

            UI ui = UI.getCurrent();

            if ( ui != null )
            {
                long delay = 20L;

                if ( goal > 0 && goal <= 10 )
                {
                    delay = 500L;
                }
                else if ( goal > 10 && goal <= 25 )
                {
                    delay = 150L;
                }
                else if ( goal > 25 && goal <= 80 )
                {
                    delay = 60L;
                }

                executor = Executors.newScheduledThreadPool( 1 );
                Runnable task = () -> {
                    if ( current > goal )
                    {
                        executor.shutdown();
                        return;
                    }
                    ui.access( () -> {
                        getElement().setProperty( "title", "" + current++ );
                        ui.push();
                    } );
                };
                executor.scheduleAtFixedRate( task, 0, delay, TimeUnit.MILLISECONDS );
            }

        }
        catch ( NumberFormatException e )
        {
            getElement().setProperty( "title", title );
        }
    }

}