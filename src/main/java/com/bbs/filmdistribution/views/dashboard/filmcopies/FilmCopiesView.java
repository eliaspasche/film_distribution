package com.bbs.filmdistribution.views.dashboard.filmcopies;

import com.bbs.filmdistribution.components.MasterDetailGridLayout;
import com.bbs.filmdistribution.data.entity.Film;
import com.bbs.filmdistribution.data.entity.FilmCopy;
import com.bbs.filmdistribution.data.service.FilmCopyService;
import com.bbs.filmdistribution.data.service.FilmService;
import com.bbs.filmdistribution.views.DynamicView;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@PageTitle( "Film Copies" )
@Route( value = "filmCopies/:filmCopyID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
public class FilmCopiesView extends MasterDetailGridLayout<FilmCopy, FilmCopyService> implements DynamicView
{
    // Route
    private static final String FILMCOPY_ID = "filmCopyID";
    private static final String FILMCOPY_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/filmCopies/%s/edit";

    // Services
    private final FilmService filmService;

    // Layout
    private H3 splitTitle;
    private TextField inventoryNumber;
    private Select<Film> film;
    private final Button saveButton = new Button( "Save" );


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
        binder.bind( film, FilmCopy::getFilm, FilmCopy::setFilm );

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

        grid.addColumn( "inventoryNumber" ).setAutoWidth( true );
        grid.addColumn( filmCopy -> filmCopy.getFilm().getName() ).setHeader( "Film" ).setAutoWidth( true );
        grid.addComponentColumn( item -> getDeleteButton( item, item.getInventoryNumber(), this ) );
        grid.setItems( query -> getDatabaseService().list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ) ).stream() );
        grid.addThemeVariants( GridVariant.LUMO_NO_BORDER );

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener( event -> {
            if ( event.getValue() != null )
            {
                UI.getCurrent().navigate( String.format( FILMCOPY_EDIT_ROUTE_TEMPLATE, event.getValue().getId() ) );
            }
            else
            {
                clearForm();
                UI.getCurrent().navigate( FilmCopiesView.class );
            }
        } );
    }

    @Override
    protected void createEditorLayout()
    {

        FormLayout formLayout = new FormLayout();

        splitTitle = new H3( "New " + getEditItemName() );

        inventoryNumber = new TextField( "Inventory Number" );
        film = new Select<>();
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

    /**
     * Create the button layout to persist the {@link Film} object.
     */
    private void createButtonLayout()
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

    @Override
    public void updateView()
    {
        refreshGrid();
    }
}
