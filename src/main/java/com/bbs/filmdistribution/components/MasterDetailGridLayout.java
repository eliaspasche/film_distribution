package com.bbs.filmdistribution.components;

import com.bbs.filmdistribution.data.entity.AbstractEntity;
import com.bbs.filmdistribution.data.service.AbstractDatabaseService;
import com.bbs.filmdistribution.util.NotificationUtil;
import com.bbs.filmdistribution.views.DynamicView;
import com.bbs.filmdistribution.wrapper.EntityDeleteDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

/**
 * Master detail grid layout to persists an {@link AbstractEntity} with an {@link AbstractDatabaseService}
 *
 * @param <T> The {@link AbstractEntity}
 * @param <K> The {@link AbstractDatabaseService}
 */
@Getter
@Setter
public abstract class MasterDetailGridLayout<T extends AbstractEntity, K extends AbstractDatabaseService<T, ?>>
        extends MasterDetailLayout implements BeforeEnterObserver
{

    private final String editId;
    private final String editRoute;

    private final K databaseService;

    private T itemToEdit;

    private BeanValidationBinder<T> binder;

    // Layout
    private Grid<T> grid;

    private Button createButton = new Button();

    /**
     * The constructor.
     *
     * @param editId          The key in url to edit a {@link AbstractEntity}
     * @param editRoute       The edit url which includes the editId
     * @param databaseService The {@link AbstractDatabaseService}
     */
    protected MasterDetailGridLayout( String editId, String editRoute, K databaseService )
    {
        this.editId = editId;
        this.editRoute = editRoute;
        this.databaseService = databaseService;
    }

    /**
     * Initialization of the components in the current view.
     */
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

    /**
     * Get the name of the entity
     *
     * @return The entity name.
     */
    protected abstract String getEditItemName();

    /**
     * Persist item in database.
     */
    protected void saveItem()
    {
        try
        {
            binder.writeBean( this.itemToEdit );
            databaseService.update( this.itemToEdit );
            clearForm();
            refreshGrid();
            NotificationUtil.sendSuccessNotification( "Data updated", 2 );
            UI.getCurrent().navigate( this.getClass() );
        }
        catch ( ObjectOptimisticLockingFailureException exception )
        {
            NotificationUtil.sendErrorNotification( "Error updating the data. Somebody else has updated the record while you were making changes", 2 );
        }
        catch ( ValidationException validationException )
        {
            NotificationUtil.sendErrorNotification( "Failed to update the data. Check again that all values are valid", 2 );
        }
    }

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

    /**
     * Returns a delete button for the given item.
     */
    protected Button getDeleteButton( T item, String identifier, DynamicView view )
    {
        Button deleteButton = new Button( new Icon( VaadinIcon.TRASH ) );
        deleteButton.setTooltipText( "Shift + Click = Instant delete" );
        deleteButton.addThemeVariants( ButtonVariant.LUMO_ERROR );
        deleteButton.addClickListener( e -> {
            if ( e.isShiftKey() )
            {
                databaseService.delete( item.getId() );
                NotificationUtil.sendSuccessNotification( "Successfully removed", 2 );
                refreshGrid();
                return;
            }
            new EntityDeleteDialog<>( "Should the item \"" + identifier + "\" removed?", databaseService, item, view );
        } );

        return deleteButton;
    }

}
