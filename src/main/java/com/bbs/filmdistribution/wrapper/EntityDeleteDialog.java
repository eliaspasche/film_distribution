package com.bbs.filmdistribution.wrapper;

import com.bbs.filmdistribution.data.entity.AbstractEntity;
import com.bbs.filmdistribution.data.service.AbstractDatabaseService;
import com.bbs.filmdistribution.util.NotificationUtil;
import com.bbs.filmdistribution.views.DynamicView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dialog to delete a {@link AbstractEntity}
 */
public class EntityDeleteDialog<T extends AbstractEntity> extends Dialog
{
    private static final Logger LOGGER = Logger.getLogger( EntityDeleteDialog.class.getName() );

    private final String title;
    private final AbstractDatabaseService<T, ?> abstractDatabaseService;
    private final T entityToDelete;
    private final DynamicView dynamicView;

    /**
     * The constructor.
     *
     * @param title                   The title to display in the dialog.
     * @param abstractDatabaseService The {@link AbstractDatabaseService}
     * @param entityToDelete          The {@link AbstractEntity} to delete
     * @param dynamicView             The {@link DynamicView}
     */
    public EntityDeleteDialog( String title, AbstractDatabaseService<T, ?> abstractDatabaseService, T entityToDelete, DynamicView dynamicView )
    {
        this.title = title;
        this.abstractDatabaseService = abstractDatabaseService;
        this.entityToDelete = entityToDelete;
        this.dynamicView = dynamicView;
        init();
    }

    public EntityDeleteDialog( String title, AbstractDatabaseService<T, ?> abstractDatabaseService, T entityToDelete )
    {
        this( title, abstractDatabaseService, entityToDelete, null );
    }

    /**
     * Build the dialog
     */
    private void init()
    {
        setHeaderTitle( title );

        Button deleteButton = new Button( "Delete" );
        deleteButton.addThemeVariants( ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR );
        deleteButton.getStyle().set( "margin-right", "auto" );
        deleteButton.addClickListener( click -> {
            try
            {
                abstractDatabaseService.delete( entityToDelete.getId() );
                NotificationUtil.sendSuccessNotification( "Successfully removed", 2 );
                if ( dynamicView != null )
                {
                    dynamicView.updateView();
                }
            }
            catch ( Exception e )
            {
                LOGGER.log( Level.WARNING, e.getMessage() );
                NotificationUtil.sendErrorNotification( "Item could not be removed. Cause: " + e.getCause(), 5 );
            }
            finally
            {
                close();
            }
        } );
        getFooter().add( deleteButton );

        Button cancelButton = new Button( "Cancel", event -> close() );
        cancelButton.addThemeVariants( ButtonVariant.LUMO_TERTIARY );
        getFooter().add( cancelButton );

        open();
    }

}
