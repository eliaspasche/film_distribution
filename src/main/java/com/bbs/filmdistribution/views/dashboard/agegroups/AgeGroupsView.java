package com.bbs.filmdistribution.views.dashboard.agegroups;

import com.bbs.filmdistribution.data.entity.AgeGroup;
import com.bbs.filmdistribution.data.service.AgeGroupService;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

@PageTitle( "Age Groups" )
@Route( value = "ageGroups/:ageGroupID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
public class AgeGroupsView extends Div implements BeforeEnterObserver
{

    private static final String AGEGROUP_ID = "ageGroupID";
    private static final String AGEGROUP_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/ageGroups/%s/edit";

    private final Grid<AgeGroup> grid = new Grid<>( AgeGroup.class, false );

    private TextField minimumAge;
    private TextField name;

    private final Button cancel = new Button( "Cancel" );
    private final Button save = new Button( "Save" );

    private final BeanValidationBinder<AgeGroup> binder;

    private AgeGroup ageGroup;

    private final AgeGroupService ageGroupService;

    public AgeGroupsView( AgeGroupService ageGroupService )
    {
        this.ageGroupService = ageGroupService;
        addClassNames( "age-groups-view" );

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout( splitLayout );
        createEditorLayout( splitLayout );

        add( splitLayout );

        // Configure Grid
        grid.addColumn( "minimumAge" ).setAutoWidth( true );
        grid.addColumn( "name" ).setAutoWidth( true );
        grid.setItems( query -> ageGroupService.list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ) ).stream() );
        grid.addThemeVariants( GridVariant.LUMO_NO_BORDER );

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener( event -> {
            if ( event.getValue() != null )
            {
                UI.getCurrent().navigate( String.format( AGEGROUP_EDIT_ROUTE_TEMPLATE, event.getValue().getId() ) );
            }
            else
            {
                clearForm();
                UI.getCurrent().navigate( AgeGroupsView.class );
            }
        } );

        // Configure Form
        binder = new BeanValidationBinder<>( AgeGroup.class );

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField( minimumAge ).withConverter( new StringToIntegerConverter( "Only numbers are allowed" ) ).bind( "minimumAge" );

        binder.bindInstanceFields( this );

        cancel.addClickListener( e -> {
            clearForm();
            refreshGrid();
        } );

        save.addClickListener( e -> {
            try
            {
                if ( this.ageGroup == null )
                {
                    this.ageGroup = new AgeGroup();
                }
                binder.writeBean( this.ageGroup );
                ageGroupService.update( this.ageGroup );
                clearForm();
                refreshGrid();
                Notification.show( "Data updated" );
                UI.getCurrent().navigate( AgeGroupsView.class );
            }
            catch ( ObjectOptimisticLockingFailureException exception )
            {
                Notification n = Notification.show( "Error updating the data. Somebody else has updated the record while you were making changes." );
                n.setPosition( Position.MIDDLE );
                n.addThemeVariants( NotificationVariant.LUMO_ERROR );
            }
            catch ( ValidationException validationException )
            {
                Notification.show( "Failed to update the data. Check again that all values are valid" );
            }
        } );
    }

    @Override
    public void beforeEnter( BeforeEnterEvent event )
    {
        Optional<Long> ageGroupId = event.getRouteParameters().get( AGEGROUP_ID ).map( Long::parseLong );
        if ( ageGroupId.isPresent() )
        {
            Optional<AgeGroup> ageGroupFromBackend = ageGroupService.get( ageGroupId.get() );
            if ( ageGroupFromBackend.isPresent() )
            {
                populateForm( ageGroupFromBackend.get() );
            }
            else
            {
                Notification.show( String.format( "The requested ageGroup was not found, ID = %s", ageGroupId.get() ), 3000, Notification.Position.BOTTOM_START );
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo( AgeGroupsView.class );
            }
        }
    }

    private void createEditorLayout( SplitLayout splitLayout )
    {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName( "editor-layout" );

        Div editorDiv = new Div();
        editorDiv.setClassName( "editor" );
        editorLayoutDiv.add( editorDiv );

        FormLayout formLayout = new FormLayout();
        minimumAge = new TextField( "Minimum Age" );
        name = new TextField( "Name" );
        formLayout.add( minimumAge, name );

        editorDiv.add( formLayout );
        createButtonLayout( editorLayoutDiv );

        splitLayout.addToSecondary( editorLayoutDiv );
    }

    private void createButtonLayout( Div editorLayoutDiv )
    {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName( "button-layout" );
        cancel.addThemeVariants( ButtonVariant.LUMO_TERTIARY );
        save.addThemeVariants( ButtonVariant.LUMO_PRIMARY );
        buttonLayout.add( save, cancel );
        editorLayoutDiv.add( buttonLayout );
    }

    private void createGridLayout( SplitLayout splitLayout )
    {
        Div wrapper = new Div();
        wrapper.setClassName( "grid-wrapper" );
        splitLayout.addToPrimary( wrapper );
        wrapper.add( grid );
    }

    private void refreshGrid()
    {
        grid.select( null );
        grid.getDataProvider().refreshAll();
    }

    private void clearForm()
    {
        populateForm( null );
    }

    private void populateForm( AgeGroup value )
    {
        this.ageGroup = value;
        binder.readBean( this.ageGroup );

    }
}
