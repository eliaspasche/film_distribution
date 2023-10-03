package com.bbs.filmdistribution.views.dashboard.films;

import com.bbs.filmdistribution.components.MasterDetailGridLayout;
import com.bbs.filmdistribution.data.entity.AgeGroup;
import com.bbs.filmdistribution.data.entity.Film;
import com.bbs.filmdistribution.data.service.AgeGroupService;
import com.bbs.filmdistribution.data.service.FilmService;
import com.bbs.filmdistribution.util.ComponentUtil;
import com.bbs.filmdistribution.util.NumbersUtil;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.bbs.filmdistribution.wrapper.GridFilter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * A view to manage the {@link Film} objects.
 */
@PageTitle( "Films" )
@Route( value = "films/:filmID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
public class FilmsView extends MasterDetailGridLayout<Film, FilmService>
{
    // Route
    private static final String FILM_ID = "filmID";
    private static final String FILM_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/films/%s/edit";

    // Services
    private final AgeGroupService ageGroupService;

    // Layout
    private H3 splitTitle;
    private TextField name;
    private IntegerField length;
    private Select<AgeGroup> ageGroup;
    private NumberField price;
    private IntegerField availableCopies;

    private final Button saveButton = new Button( "Save" );

    /**
     * The constructor.
     *
     * @param filmService     The {@link FilmService}
     * @param ageGroupService The {@link AgeGroupService}
     */
    public FilmsView( FilmService filmService, AgeGroupService ageGroupService )
    {
        super( FILM_ID, FILM_EDIT_ROUTE_TEMPLATE, filmService );
        this.ageGroupService = ageGroupService;
        setCreateButton( new Button( "New " + getEditItemName() ) );
    }

    @Override
    protected void defineValidator()
    {
        // Configure Form
        BeanValidationBinder<Film> binder = new BeanValidationBinder<>( Film.class );
        setBinder( binder );

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField( name ).asRequired().bind( "name" );
        binder.forField( length ).asRequired().bind( "length" );
        binder.forField( price ).asRequired().bind( "price" );
        binder.forField( availableCopies ).asRequired().bind( "availableCopies" );
        binder.forField( ageGroup ).asRequired().bind( Film::getAgeGroup, Film::setAgeGroup );

        binder.bindInstanceFields( this );

        getCreateButton().addClickListener( e -> {
            clearForm();
            refreshGrid();
        } );

        saveButton.addClickListener( e -> {
            if ( getItemToEdit() == null )
            {
                setItemToEdit( new Film() );
            }
            saveItem();
        } );
    }

    @Override
    protected void buildGrid()
    {
        // Configure Grid
        Grid<Film> grid = new Grid<>( Film.class, false );
        setGrid( grid );

        grid.addThemeVariants( GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER );

        grid.setItems( query -> getDatabaseService().list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ) ).stream() );

        Grid.Column<Film> filmNameColumn = grid.addColumn( "name" ).setAutoWidth( true );
        grid.addColumn( "length" ).setAutoWidth( true );
        grid.addColumn( item -> item.getAgeGroup().getName() ).setHeader( "Age Group" ).setAutoWidth( true );
        grid.addColumn( item -> NumbersUtil.formatCurrency( item.getPrice() ), "price" ).setHeader( "Price/Week" ).setAutoWidth( true );
        grid.addColumn(item -> getDatabaseService().availableCopies(item.getId())).setHeader("Available Copies").setAutoWidth(true);
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

        // Create search filter
        HeaderRow headerRow = grid.appendHeaderRow();

        GridFilter<Film> gridFilter = new GridFilter<>( grid, getDatabaseService() );

        TextField filterTextField = ComponentUtil.createGridSearchField( "Search" );
        filterTextField.setValueChangeMode( ValueChangeMode.LAZY );
        filterTextField.addValueChangeListener( e -> gridFilter.filterFieldName( e.getValue(), "name" ) );

        headerRow.getCell( filmNameColumn ).setComponent( filterTextField );
    }

    @Override
    protected void createEditorLayout()
    {
        FormLayout formLayout = new FormLayout();

        splitTitle = new H3( "New " + getEditItemName() );

        name = new TextField( "Name" );
        length = ComponentUtil.createIntegerField( "Length", "sec." );
        length.setMax( 99999 );

        ageGroup = new Select<>();
        ageGroup.setLabel( "Age Group" );
        ageGroup.setEmptySelectionAllowed( false );
        ageGroup.setItems( ageGroupService.list( Pageable.unpaged() ).stream().toList() );
        ageGroup.setItemLabelGenerator( AgeGroup::getName );

        price = ComponentUtil.createNumberField("Price/Week", "€/week");
        price.setMax( 99.99 );

        availableCopies = ComponentUtil.createIntegerField( "Available Copies", "pieces" );

        formLayout.add( name, length, ageGroup, price, availableCopies );

        getEditorDiv().add( splitTitle, formLayout );

        createButtonLayout();
    }

    @Override
    protected String getEditItemName()
    {
        return "Film";
    }

    @Override
    protected void createButtonLayout()
    {
        getCreateButton().addThemeVariants( ButtonVariant.LUMO_TERTIARY );
        saveButton.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

        getButtonLayout().add( saveButton, getCreateButton() );
    }

    @Override
    protected void populateForm( Film value )
    {
        super.populateForm( value );
        splitTitle.setText( ( getItemToEdit() == null ? "New" : "Edit" ) + " " + getEditItemName() );
        availableCopies.setVisible( value == null || value.getId() == null );
    }

}