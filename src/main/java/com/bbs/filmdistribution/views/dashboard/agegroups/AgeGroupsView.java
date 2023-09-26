package com.bbs.filmdistribution.views.dashboard.agegroups;

import com.bbs.filmdistribution.components.MasterDetailGridLayout;
import com.bbs.filmdistribution.data.entity.AgeGroup;
import com.bbs.filmdistribution.data.service.AgeGroupService;
import com.bbs.filmdistribution.util.ComponentUtil;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;

/**
 * A view to manage the {@link AgeGroup} objects.
 */
@PageTitle( "Age Groups" )
@Route( value = "ageGroups/:ageGroupID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
public class AgeGroupsView extends MasterDetailGridLayout<AgeGroup, AgeGroupService>
{
    // Route
    private static final String AGEGROUP_ID = "ageGroupID";
    private static final String AGEGROUP_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/ageGroups/%s/edit";


    // Layout
    private H3 splitTitle;
    private IntegerField minimumAge;
    private TextField name;

    private final Button saveButton = new Button( "Save" );

    /**
     * The constructor.
     *
     * @param ageGroupService The {@link AgeGroupService}
     */
    public AgeGroupsView( AgeGroupService ageGroupService )
    {
        super( AGEGROUP_ID, AGEGROUP_EDIT_ROUTE_TEMPLATE, ageGroupService );
        setCreateButton( new Button( "New " + getEditItemName() ) );
    }

    @Override
    protected void defineValidator()
    {
        // Configure Form
        BeanValidationBinder<AgeGroup> binder = new BeanValidationBinder<>( AgeGroup.class );
        setBinder( binder );

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField( name ).asRequired().bind( "name" );
        binder.forField( minimumAge ).asRequired().bind( "minimumAge" );

        binder.bindInstanceFields( this );

        getCreateButton().addClickListener( e -> {
            clearForm();
            refreshGrid();
        } );

        saveButton.addClickListener( e -> {
            if ( getItemToEdit() == null )
            {
                setItemToEdit( new AgeGroup() );
            }
            saveItem();
        } );
    }

    @Override
    protected void buildGrid()
    {
        // Configure Grid
        Grid<AgeGroup> grid = new Grid<>( AgeGroup.class, false );
        setGrid( grid );

        grid.addThemeVariants( GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER );
        grid.setItems( query -> getDatabaseService().list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ) ).stream() );

        grid.addColumn( "name" ).setAutoWidth( true );
        grid.addColumn( "minimumAge" ).setAutoWidth( true );
        grid.addComponentColumn( item -> buildMenuBar( item, item.getName(), this ) ).setFrozenToEnd( true );

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener( event -> {
            if ( event.getValue() != null )
            {
                UI.getCurrent().navigate( String.format( getEditRoute(), event.getValue().getId() ) );
            }
            else
            {
                clearForm();
                UI.getCurrent().navigate( this.getClass() );
            }
        } );
    }

    @Override
    protected void createEditorLayout()
    {
        FormLayout formLayout = new FormLayout();

        splitTitle = new H3( "New " + getEditItemName() );
        name = new TextField( "Name" );
        minimumAge = ComponentUtil.createIntegerField( "Minimum age", "years" );

        formLayout.add( name, minimumAge );

        getEditorDiv().add( splitTitle, formLayout );

        createButtonLayout();
    }

    @Override
    protected String getEditItemName()
    {
        return "Age Group";
    }

    @Override
    protected void createButtonLayout()
    {
        getCreateButton().addThemeVariants( ButtonVariant.LUMO_TERTIARY );
        saveButton.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

        getButtonLayout().add( saveButton, getCreateButton() );
    }

    @Override
    protected void populateForm( AgeGroup value )
    {
        super.populateForm( value );
        splitTitle.setText( ( getItemToEdit() == null ? "New" : "Edit" ) + " " + getEditItemName() );
    }

}