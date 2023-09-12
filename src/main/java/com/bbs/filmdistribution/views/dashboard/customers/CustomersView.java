package com.bbs.filmdistribution.views.dashboard.customers;

import com.bbs.filmdistribution.components.MasterDetailGridLayout;
import com.bbs.filmdistribution.data.entity.Customer;
import com.bbs.filmdistribution.data.service.CustomerService;
import com.bbs.filmdistribution.util.ComponentUtil;
import com.bbs.filmdistribution.util.CustomerNumberUtil;
import com.bbs.filmdistribution.util.DateUtil;
import com.bbs.filmdistribution.views.DynamicView;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.bbs.filmdistribution.wrapper.GridFilter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;

/**
 * A view to manage the {@link Customer} objects.
 */
@PageTitle( "Customers" )
@Route( value = "customers/:customerID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
public class CustomersView extends MasterDetailGridLayout<Customer, CustomerService> implements DynamicView
{
    // Route
    private static final String CUSTOMER_ID = "customerID";
    private static final String CUSTOMER_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/customers/%s/edit";

    // Layout
    private H3 splitTitle;
    private TextField firstName;
    private TextField lastName;
    private DatePicker dateOfBirth;
    private TextField address;
    private TextField zipCode;
    private TextField city;

    private final Button saveButton = new Button( "Save" );

    /**
     * The constructor.
     *
     * @param databaseService The {@link CustomerService}
     */
    protected CustomersView( CustomerService databaseService )
    {
        super( CUSTOMER_ID, CUSTOMER_EDIT_ROUTE_TEMPLATE, databaseService );
        setCreateButton( new Button( "New " + getEditItemName() ) );
    }

    @Override
    protected void buildGrid()
    {
        // Configure Grid
        Grid<Customer> grid = new Grid<>( Customer.class, false );
        setGrid( grid );

        grid.addThemeVariants( GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER );

        grid.setItems( query -> getDatabaseService().list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ) ).stream() );

        grid.addColumn( item -> CustomerNumberUtil.createLeadingZeroCustomerNumber( item.getId() ) ).setHeader( "ID" ).setAutoWidth( true );
        Grid.Column<Customer> lastNameColumn = grid.addColumn( "name" ).setAutoWidth( true );
        Grid.Column<Customer> firstNameColumn = grid.addColumn( "firstName" ).setAutoWidth( true );
        grid.addColumn( DateUtil.createLocalDateRenderer( Customer::getDateOfBirth ) ).setHeader( "Date of birth" ).setAutoWidth( true );

        Grid.Column<Customer> addressColumn = grid.addColumn( "address" ).setAutoWidth( true );
        Grid.Column<Customer> zipCodeColumn = grid.addColumn( "zipCode" ).setAutoWidth( true );
        Grid.Column<Customer> cityColumn = grid.addColumn( "city" ).setAutoWidth( true );
        grid.addComponentColumn( item -> getDeleteButton( item, item.getName(), this ) ).setFrozenToEnd( true ).setAutoWidth( true );

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

        // Create search filter
        HeaderRow headerRow = grid.appendHeaderRow();

        GridFilter<Customer> gridFilter = new GridFilter<>( grid, getDatabaseService() );

        headerRow.getCell( lastNameColumn ).setComponent( ComponentUtil.createGridSearchField( gridFilter, "name" ) );
        headerRow.getCell( firstNameColumn ).setComponent( ComponentUtil.createGridSearchField( gridFilter, "firstName" ) );
        headerRow.getCell( addressColumn ).setComponent( ComponentUtil.createGridSearchField( gridFilter, "address" ) );
        headerRow.getCell( zipCodeColumn ).setComponent( ComponentUtil.createGridSearchField( gridFilter, "zipCode" ) );
        headerRow.getCell( cityColumn ).setComponent( ComponentUtil.createGridSearchField( gridFilter, "city" ) );

    }

    @Override
    protected void defineValidator()
    {
        // Configure Form
        BeanValidationBinder<Customer> binder = new BeanValidationBinder<>( Customer.class );
        setBinder( binder );

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField( firstName ).asRequired().bind( "firstName" );
        binder.forField( lastName ).asRequired().bind( "name" );
        binder.forField( dateOfBirth ).asRequired().bind( "dateOfBirth" );
        binder.forField( address ).asRequired().bind( "address" );
        binder.forField( zipCode ).asRequired().bind( "zipCode" );
        binder.forField( city ).asRequired().bind( "city" );

        binder.bindInstanceFields( this );

        getCreateButton().addClickListener( e -> {
            clearForm();
            refreshGrid();
        } );

        saveButton.addClickListener( e -> {
            if ( getItemToEdit() == null )
            {
                setItemToEdit( new Customer() );
            }
            saveItem();
        } );
    }

    @Override
    protected void createEditorLayout()
    {
        FormLayout formLayout = new FormLayout();

        splitTitle = new H3( "New " + getEditItemName() );

        firstName = new TextField( "Firstname" );
        lastName = new TextField( "Lastname" );
        dateOfBirth = new DatePicker( "Date of birth" );
        address = new TextField( "Address" );
        zipCode = new TextField( "Zip-Code" );
        city = new TextField( "City" );

        formLayout.add( firstName, lastName, dateOfBirth, address, zipCode, city );

        getEditorDiv().add( splitTitle, formLayout );

        createButtonLayout();
    }

    @Override
    protected void createButtonLayout()
    {
        getCreateButton().addThemeVariants( ButtonVariant.LUMO_TERTIARY );
        saveButton.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

        getButtonLayout().add( saveButton, getCreateButton() );
    }

    @Override
    protected String getEditItemName()
    {
        return "Customer";
    }

    @Override
    protected void populateForm( Customer value )
    {
        super.populateForm( value );
        splitTitle.setText( ( getItemToEdit() == null ? "New" : "Edit" ) + " " + getEditItemName() );
    }

    @Override
    public void updateView()
    {
        refreshGrid();
    }
}
