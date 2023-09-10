package com.bbs.filmdistribution.views.dashboard.newdistribution;

import com.bbs.filmdistribution.components.MasterDetailGridLayout;
import com.bbs.filmdistribution.data.entity.Customer;
import com.bbs.filmdistribution.data.entity.FilmCopy;
import com.bbs.filmdistribution.data.entity.FilmDistribution;
import com.bbs.filmdistribution.data.service.CustomerService;
import com.bbs.filmdistribution.data.service.FilmCopyService;
import com.bbs.filmdistribution.data.service.FilmDistributionService;
import com.bbs.filmdistribution.util.CustomerNumberUtil;
import com.bbs.filmdistribution.views.DynamicView;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * A view to manage the {@link FilmDistribution} objects.
 */
@PageTitle( "New Distribution" )
@Route( value = "distribution/:distributionID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
@Uses( Icon.class )
public class NewDistributionView extends MasterDetailGridLayout<FilmDistribution, FilmDistributionService> implements DynamicView
{

    // Route
    private static final String DISTRIBUTION_ID = "distributionID";
    private static final String DISTRIBUTION_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/distribution/%s/edit";

    // Services
    private final CustomerService customerService;
    private final FilmCopyService filmCopyService;

    // Layout
    private H3 splitTitle;
    private Select<Customer> customer;
    private MultiSelectComboBox<FilmCopy> filmCopies;
    private DatePicker startDate;
    private DatePicker endDate;

    private final Button saveButton = new Button( "Save" );

    /**
     * The constructor.
     *
     * @param distributionService The {@link FilmDistributionService}
     * @param customerService     The {@link CustomerService}
     * @param filmCopyService     The {@link FilmCopyService}
     */
    public NewDistributionView( FilmDistributionService distributionService,
                                CustomerService customerService, FilmCopyService filmCopyService )
    {
        super( DISTRIBUTION_ID, DISTRIBUTION_EDIT_ROUTE_TEMPLATE, distributionService );
        this.customerService = customerService;
        this.filmCopyService = filmCopyService;
        setCreateButton( new Button( "New " + getEditItemName() ) );
    }

    @Override
    protected void buildGrid()
    {
        // Configure Grid
        Grid<FilmDistribution> grid = new Grid<>( FilmDistribution.class, false );
        setGrid( grid );

        grid.addThemeVariants( GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER );

        grid.setItems( query -> getDatabaseService().list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ) ).stream() );

        grid.addColumn( item -> CustomerNumberUtil.createLeadingZeroCustomerNumber( item.getId() ) ).setHeader( "ID" ).setAutoWidth( true );
        grid.addColumn( item -> item.getCustomer().getFirstName() + " " + item.getCustomer().getName() ).setHeader( "Customer" ).setAutoWidth( true );
        grid.addColumn( "startDate" ).setAutoWidth( true );
        grid.addColumn( "endDate" ).setAutoWidth( true );
        grid.addComponentColumn( this::createReadOnlyFilmCopySelectBox ).setHeader( "Film Copies" ).setAutoWidth( true );
        grid.addComponentColumn( item -> getDeleteButton( item, item.getId().toString(), this ) );

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

    /**
     * Create read only select box to display the {@link FilmCopy} for a {@link FilmDistribution}.
     *
     * @param filmDistribution The {@link FilmDistribution}
     * @return The {@link Select} component
     */
    private Select<FilmCopy> createReadOnlyFilmCopySelectBox( FilmDistribution filmDistribution )
    {
        Select<FilmCopy> filmCopySelect = new Select<>();
        filmCopySelect.setItems( filmDistribution.getFilmCopies() );
        filmCopySelect.setItemLabelGenerator( c -> c.getFilm().getName() );

        return filmCopySelect;
    }

    @Override
    protected void defineValidator()
    {
        // Configure Form
        BeanValidationBinder<FilmDistribution> binder = new BeanValidationBinder<>( FilmDistribution.class );
        setBinder( binder );

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField( customer ).asRequired().bind( FilmDistribution::getCustomer, FilmDistribution::setCustomer );
        binder.forField( filmCopies ).asRequired().bind( FilmDistribution::getFilmCopies, FilmDistribution::setFilmCopies );
        binder.forField( startDate ).asRequired().bind( "startDate" );
        binder.forField( endDate ).asRequired().bind( "endDate" );

        binder.bindInstanceFields( this );

        getCreateButton().addClickListener( e -> {
            clearForm();
            refreshGrid();
        } );

        saveButton.addClickListener( e -> {
            if ( getItemToEdit() == null )
            {
                setItemToEdit( new FilmDistribution() );
            }
            saveItem();
        } );
        
    }

    @Override
    protected void createEditorLayout()
    {
        LocalDate now = LocalDate.now( ZoneId.systemDefault() );

        FormLayout formLayout = new FormLayout();

        splitTitle = new H3( "New " + getEditItemName() );

        customer = new Select<>();
        customer.setLabel( "Customer" );
        customer.setEmptySelectionAllowed( false );
        customer.setItems( customerService.list( Pageable.unpaged() ).stream().toList() );
        customer.setItemLabelGenerator( c -> c.getFirstName() + " " + c.getName() );

        filmCopies = new MultiSelectComboBox<>( "Film Copies" );
        filmCopies.setItems( filmCopyService.getAvailableCopies() );
        filmCopies.setItemLabelGenerator( c -> c.getInventoryNumber() + " (" + c.getFilm().getName() + ")" );

        startDate = new DatePicker( "Start Date" );
        endDate = new DatePicker( "End Date" );

        startDate.addValueChangeListener( event -> {
            if ( event.getValue() == null )
            {
                return;
            }
            endDate.setMin( startDate.getValue().plusDays( 7 ) );
            endDate.setMax( startDate.getValue().plusDays( 90 ) );
        } );
        startDate.setValue( now );

        formLayout.add( customer, filmCopies, startDate, endDate );

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
        return "Film Distribution";
    }

    @Override
    protected void populateForm( FilmDistribution value )
    {
        super.populateForm( value );
        splitTitle.setText( ( getItemToEdit() == null ? "New" : "Edit" ) + " " + getEditItemName() );

        if ( filmCopies != null && getItemToEdit() != null )
        {
            List<FilmCopy> filmCopyList = new ArrayList<>();
            filmCopyList.addAll( filmCopyService.getAvailableCopies() );
            filmCopyList.addAll( getItemToEdit().getFilmCopies() );
            filmCopies.setItems( filmCopyList );
            filmCopies.setValue( getItemToEdit().getFilmCopies() );
        }
    }

    @Override
    public void updateView()
    {
        refreshGrid();
    }

    @Override
    protected void refreshGrid()
    {
        super.refreshGrid();
        if ( filmCopies != null )
        {
            filmCopies.setItems( filmCopyService.getAvailableCopies() );
        }
    }
}
