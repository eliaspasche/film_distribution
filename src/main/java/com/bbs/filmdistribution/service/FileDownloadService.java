package com.bbs.filmdistribution.service;

import com.bbs.filmdistribution.util.NotificationUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service to manage file downloads.
 */
@Component
public class FileDownloadService
{

    private static final Logger LOGGER = Logger.getLogger( FileDownloadService.class.getName() );

    /**
     * Download a file by a path.
     *
     * @param filePath The file path.
     */
    public void downloadFileFromStreamLink( String filePath )
    {
        File file = new File( filePath );
        if ( !file.exists() )
        {
            NotificationUtil.sendErrorNotification( "The requested file does not exists", 2 );
            return;
        }

        final StreamRegistration registration = VaadinSession.getCurrent().getResourceRegistry()
                .registerResource( getStreamResourceFromFile( file ) );

        UI.getCurrent().getPage().open( registration.getResourceUri().toString(), file.getName().endsWith( ".pdf" ) ? "_blank" : "_parent" );
    }

    /**
     * Build the {@link StreamResource} from a {@link File}.
     *
     * @param file The {@link File}
     * @return The created {@link StreamResource}
     */
    public StreamResource getStreamResourceFromFile( File file )
    {
        return new StreamResource( file.getName(),
                () -> {
                    try
                    {
                        return new FileInputStream( file.getPath() );
                    }
                    catch ( FileNotFoundException e )
                    {
                        LOGGER.log( Level.WARNING, e.getMessage(), e );
                        return null;
                    }
                } );
    }
}
