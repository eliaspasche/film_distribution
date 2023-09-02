package com.bbs.filmdistribution.views.dashboard.agegroups;

import com.bbs.filmdistribution.components.MasterDetailGridLayout;
import com.bbs.filmdistribution.data.entity.AgeGroup;
import com.bbs.filmdistribution.data.service.AgeGroupService;
import com.bbs.filmdistribution.util.ComponentUtil;
import com.bbs.filmdistribution.views.DynamicView;
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

@PageTitle( "Age Groups" )
@Route( value = "ageGroups/:ageGroupID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
public class AgeGroupsView extends MasterDetailGridLayout<AgeGroup, AgeGroupService> implements DynamicView
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
        this.createButton = new Button( "New " + getEditItemName() );
    }

    @Override
    protected void defineValidator()
    {
        // Configure Form
        binder = new BeanValidationBinder<>( AgeGroup.class );

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField( name ).asRequired().bind( "name" );
        binder.forField( minimumAge ).asRequired().bind( "minimumAge" );

        binder.bindInstanceFields( this );

        createButton.addClickListener( e -> {
            clearForm();
            refreshGrid();
        } );

        saveButton.addClickListener( e -> {
            if ( this.itemToEdit == null )
            {
                this.itemToEdit = new AgeGroup();
            }
            saveItem();
        } );
    }

    @Override
    protected void buildGrid()
    {
        // Configure Grid
        grid = new Grid<>( AgeGroup.class, false );

        grid.addThemeVariants( GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER );
        grid.setItems( query -> databaseService.list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ) ).stream() );

        grid.addColumn( "name" ).setAutoWidth( true );
        grid.addColumn( "minimumAge" ).setAutoWidth( true );
        grid.addComponentColumn(item -> getDeleteButton(item, item.getName(), this));


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

    /**
     * Create the button layout to persist the {@link AgeGroup} object.
     */
    private void createButtonLayout()
    {
        createButton.addThemeVariants( ButtonVariant.LUMO_TERTIARY );
        saveButton.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

        getButtonLayout().add( saveButton, createButton );
    }

    @Override
    protected void populateForm( AgeGroup value )
    {
        super.populateForm( value );
        splitTitle.setText( ( this.itemToEdit == null ? "New" : "Edit" ) + " " + getEditItemName() );
    }

    @Override
    public void updateView()
    {
        refreshGrid();
    }

}