package com.bbs.filmdistribution.views.dashboard.filmcopies;

import com.bbs.filmdistribution.components.MasterDetailGridLayout;
import com.bbs.filmdistribution.data.entity.Film;
import com.bbs.filmdistribution.data.entity.FilmCopy;
import com.bbs.filmdistribution.data.service.FilmCopyService;
import com.bbs.filmdistribution.data.service.FilmService;
import com.bbs.filmdistribution.util.ComponentUtil;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.bbs.filmdistribution.wrapper.GridFilter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * A view to manage the {@link FilmCopy} objects.
 */
@PageTitle( "Film Copies" )
@Route( value = "filmCopies/:filmCopyID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
public class FilmCopiesView extends MasterDetailGridLayout<FilmCopy, FilmCopyService>
{
    // Route
    private static final String FILMCOPY_ID = "filmCopyID";
    private static final String FILMCOPY_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/filmCopies/%s/edit";

    // Services
    private final FilmService filmService;

    // Layout
    private H3 splitTitle;
    private TextField inventoryNumber;
    private ComboBox<Film> film;
    private final Button saveButton = new Button( "Save" );

    /**
     * The constructor.
     *
     * @param filmCopyService The {@link FilmCopyService}
     * @param filmService     The {@link FilmService}
     */
    protected FilmCopiesView( FilmCopyService filmCopyService, FilmService filmService )
    {
        super( FILMCOPY_ID, FILMCOPY_EDIT_ROUTE_TEMPLATE, filmCopyService );
        this.filmService = filmService;
        setCreateButton( new Button( "New " + getEditItemName() ) );
    }

    @Override
    protected void defineValidator()
    {
        // Configure Form
        BeanValidationBinder<FilmCopy> binder = new BeanValidationBinder<>( FilmCopy.class );
        setBinder( binder );

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField( inventoryNumber ).asRequired().bind( "inventoryNumber" );
        binder.forField( film ).asRequired().bind( FilmCopy::getFilm, FilmCopy::setFilm );

        binder.bindInstanceFields( this );

        getCreateButton().addClickListener( e -> {
            clearForm();
            refreshGrid();
        } );

        saveButton.addClickListener( e -> {
            if ( getItemToEdit() == null )
            {
                setItemToEdit( new FilmCopy() );
            }
            saveItem();
        } );
    }


    @Override
    protected void buildGrid()
    {
        Grid<FilmCopy> grid = new Grid<>( FilmCopy.class, false );
        setGrid( grid );

        grid.addThemeVariants( GridVariant.LUMO_NO_BORDER );
        grid.setItems( query -> getDatabaseService().list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ) ).stream() );

        grid.addColumn( "inventoryNumber" ).setAutoWidth( true );
        Grid.Column<FilmCopy> filmNameColumn = grid.addColumn( filmCopy -> filmCopy.getFilm().getName() )
                .setHeader( "Film" ).setAutoWidth( true );
        grid.addComponentColumn(this::createCopyAvailableBadge).setHeader("Availability").setAutoWidth(true);
        grid.addComponentColumn( item -> buildMenuBar( item, item.getInventoryNumber(), this ) ).setFrozenToEnd( true );

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

        GridFilter<FilmCopy> gridFilter = new GridFilter<>( grid, getDatabaseService() );

        TextField filterTextField = ComponentUtil.createGridSearchField( "Search" );
        filterTextField.setValueChangeMode( ValueChangeMode.LAZY );
        filterTextField.addValueChangeListener( e -> gridFilter.filterFieldName( e.getValue(), "film", "name" ) );

        headerRow.getCell( filmNameColumn ).setComponent( filterTextField );
    }

    /**
     * Create badge to display the available state of a {@link FilmCopy}
     *
     * @param filmCopy The {@link FilmCopy}
     * @return The {@link Icon}
     */
    private Icon createCopyAvailableBadge( FilmCopy filmCopy )
    {
        boolean copyAvailable = getDatabaseService().isFilmCopyAvailable( filmCopy.getId() );
        VaadinIcon icon = copyAvailable ? VaadinIcon.CHECK : VaadinIcon.CLOSE;
        String styleLabel = "badge " + ( copyAvailable ? "success" : "error" );

        Icon confirmed = createIcon( icon, copyAvailable ? "Available" : "Not Available" );
        confirmed.getElement().getThemeList().add( styleLabel );
        return confirmed;
    }

    /**
     * Create {@link VaadinIcon} with label.
     *
     * @param vaadinIcon The {@link VaadinIcon}
     * @param label      The label
     * @return The created {@link Icon}
     */
    private Icon createIcon( VaadinIcon vaadinIcon, String label )
    {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set( "padding", "var(--lumo-space-xs)" );
        // Accessible label
        icon.getElement().setAttribute( "aria-label", label );
        icon.setTooltipText( label );

        return icon;
    }

    @Override
    protected void createEditorLayout()
    {
        FormLayout formLayout = new FormLayout();

        splitTitle = new H3( "New " + getEditItemName() );

        Button generateUuidButton = new Button( "UUID" );
        generateUuidButton.setTooltipText( "Generate UUID" );
        generateUuidButton.addThemeVariants( ButtonVariant.LUMO_SMALL );

        inventoryNumber = new TextField( "Inventory Number" );
        inventoryNumber.setSuffixComponent( generateUuidButton );

        generateUuidButton.addClickListener( click -> inventoryNumber.setValue( UUID.randomUUID().toString() ) );

        film = new ComboBox<>();
        film.setLabel( "Film" );
        film.setItems( filmService.list( Pageable.unpaged() ).stream().toList() );
        film.setItemLabelGenerator( Film::getName );

        formLayout.add( inventoryNumber, film );

        getEditorDiv().add( splitTitle, formLayout );

        createButtonLayout();
    }

    @Override
    protected String getEditItemName()
    {
        return "Film Copy";
    }

    @Override
    protected void createButtonLayout()
    {
        getCreateButton().addThemeVariants( ButtonVariant.LUMO_TERTIARY );
        saveButton.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

        getButtonLayout().add( saveButton, getCreateButton() );
    }


    @Override
    protected void populateForm( FilmCopy value )
    {
        super.populateForm( value );
        splitTitle.setText( ( getItemToEdit() == null ? "New" : "Edit" ) + " " + getEditItemName() );
    }
}