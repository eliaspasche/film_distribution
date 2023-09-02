package com.bbs.filmdistribution.components;

import com.bbs.filmdistribution.data.entity.AbstractEntity;
import com.bbs.filmdistribution.data.service.AbstractDatabaseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import jakarta.annotation.PostConstruct;

import java.util.Optional;

public abstract class MasterDetailGridLayout<T extends AbstractEntity, K extends AbstractDatabaseService<T>>
        extends MasterDetailLayout implements BeforeEnterObserver
{

    protected final String editId;
    protected final String editRoute;

    protected final K databaseService;

    protected T itemToEdit;

    protected BeanValidationBinder<T> binder;

    // Layout
    protected Grid<T> grid;

    protected Button createButton = new Button();

    protected MasterDetailGridLayout( String editId, String editRoute, K databaseService )
    {
        this.editId = editId;
        this.editRoute = editRoute;
        this.databaseService = databaseService;
    }

    @PostConstruct
    public void init()
    {
        buildGrid();
        getPrimaryDiv().add( grid );
        createEditorLayout();

        defineValidator();
    }

    /**
     * Build the {@link Grid} component which includes the available objects.
     */
    protected abstract void buildGrid();

    /**
     * Define the validator for a specific object.
     */
    protected abstract void defineValidator();

    /**
     * Create the layout to edit a defined entity
     */
    protected abstract void createEditorLayout();

    @Override
    public void beforeEnter( BeforeEnterEvent event )
    {
        Optional<Long> entityId = event.getRouteParameters().get( editId ).map( Long::parseLong );
        createButton.setVisible( entityId.isPresent() );
        if ( entityId.isPresent() )
        {
            Optional<T> filmFromBackend = databaseService.get( entityId.get() );
            if ( filmFromBackend.isPresent() )
            {
                populateForm( filmFromBackend.get() );
            }
            else
            {
                Notification.show( String.format( "The requested film was not found, ID = %s", entityId.get() ), 3000, Notification.Position.BOTTOM_START );
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo( this.getClass() );
            }
        }
    }

    /**
     * Refresh the {@link Grid}
     */
    protected void refreshGrid()
    {
        grid.select( null );
        grid.getDataProvider().refreshAll();
    }

    /**
     * Update the editor layout with a specific object.
     *
     * @param value The object
     */
    protected void populateForm( T value )
    {
        this.itemToEdit = value;
        binder.readBean( this.itemToEdit );
    }

    /**
     * Clear all input fields.
     */
    protected void clearForm()
    {
        populateForm( null );
    }

}
