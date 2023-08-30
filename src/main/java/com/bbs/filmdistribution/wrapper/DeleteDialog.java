package com.bbs.filmdistribution.wrapper;

import com.bbs.filmdistribution.data.entity.AbstractEntity;
import com.bbs.filmdistribution.data.service.AbstractDatabaseService;
import com.bbs.filmdistribution.util.NotificationUtil;
import com.bbs.filmdistribution.views.DynamicView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;

/**
 * Dialog to delete a {@link AbstractEntity}
 */
public class DeleteDialog extends Dialog
{
    private final String title;
    private final AbstractDatabaseService abstractDatabaseService;
    private final AbstractEntity entityToDelete;
    private final DynamicView dynamicView;

    /**
     * The constructor.
     *
     * @param title                   The title to display in the dialog.
     * @param abstractDatabaseService The {@link AbstractDatabaseService}
     * @param entityToDelete          The {@link AbstractEntity} to delete
     * @param dynamicView             The {@link DynamicView}
     */
    public DeleteDialog( String title, AbstractDatabaseService abstractDatabaseService, AbstractEntity entityToDelete, DynamicView dynamicView )
    {
        this.title = title;
        this.abstractDatabaseService = abstractDatabaseService;
        this.entityToDelete = entityToDelete;
        this.dynamicView = dynamicView;
        init();
    }

    public DeleteDialog( String title, AbstractDatabaseService abstractDatabaseService, AbstractEntity entityToDelete )
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
            abstractDatabaseService.delete( entityToDelete.getId() );
            NotificationUtil.sendSuccessNotification( "Erfolgreich entfernt", 2 );
            if ( dynamicView != null )
            {
                dynamicView.updateView();
            }
            close();
        } );
        getFooter().add( deleteButton );

        Button cancelButton = new Button( "Cancel", event -> close() );
        cancelButton.addThemeVariants( ButtonVariant.LUMO_TERTIARY );
        getFooter().add( cancelButton );

        open();
    }

}
