package com.bbs.filmdistribution.views.dashboard.filmcopies;

import com.bbs.filmdistribution.data.entity.Film;
import com.bbs.filmdistribution.data.entity.FilmCopy;
import com.bbs.filmdistribution.data.service.FilmCopyService;
import com.bbs.filmdistribution.data.service.FilmService;
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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

@PageTitle( "Film Copies" )
@Route( value = "filmCopies/:filmCopyID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
@RequiredArgsConstructor
public class FilmCopiesView extends Div implements BeforeEnterObserver
{
    // Route
    private static final String FILMCOPY_ID = "filmCopyID";
    private static final String FILMCOPY_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/filmCopies/%s/edit";

    // Services
    private final FilmCopyService filmCopyService;
    private final FilmService filmService;

    // Layout
    private final Grid<FilmCopy> grid = new Grid<>( FilmCopy.class, false );
    private Select<Film> film;
    private final Button cancel = new Button( "Cancel" );
    private final Button save = new Button( "Save" );

    // Validation
    private BeanValidationBinder<FilmCopy> binder;

    private FilmCopy filmCopy;

    @PostConstruct
    public void init()
    {
        addClassNames( "film-copies-view" );

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout( splitLayout );
        createEditorLayout( splitLayout );

        add( splitLayout );

        // Configure Grid
        grid.addColumn( "inventoryNumber" ).setAutoWidth( true );
        grid.addColumn( filmCopy -> filmCopy.getFilm().getName() ).setHeader( "Film" ).setAutoWidth( true );
        grid.setItems( query -> filmCopyService.list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ) ).stream() );
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

        // Configure Form
        binder = new BeanValidationBinder<>( FilmCopy.class );

        // Bind fields. This is where you'd define e.g. validation rules
        binder.bind( film, FilmCopy::getFilm, FilmCopy::setFilm );
        binder.bindInstanceFields( this );

        cancel.addClickListener( e -> {
            clearForm();
            refreshGrid();
        } );

        save.addClickListener( e -> {
            try
            {
                if ( this.filmCopy == null )
                {
                    this.filmCopy = new FilmCopy();
                }
                binder.writeBean( this.filmCopy );
                filmCopyService.update( this.filmCopy );
                clearForm();
                refreshGrid();
                Notification.show( "Data updated" );
                UI.getCurrent().navigate( FilmCopiesView.class );
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
        Optional<Long> filmCopyId = event.getRouteParameters().get( FILMCOPY_ID ).map( Long::parseLong );
        if ( filmCopyId.isPresent() )
        {
            Optional<FilmCopy> filmCopyFromBackend = filmCopyService.get( filmCopyId.get() );
            if ( filmCopyFromBackend.isPresent() )
            {
                populateForm( filmCopyFromBackend.get() );
            }
            else
            {
                Notification.show( String.format( "The requested filmCopy was not found, ID = %s", filmCopyId.get() ), 3000, Notification.Position.BOTTOM_START );
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo( FilmCopiesView.class );
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
        TextField inventoryNumber = new TextField( "Inventory Id" );
        film = new Select<>();
        film.setLabel( "Film" );
        film.setItems( filmService.list( Pageable.unpaged() ).stream().toList() );
        film.setItemLabelGenerator( Film::getName );

        formLayout.add( inventoryNumber, film );

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

    private void populateForm( FilmCopy value )
    {
        this.filmCopy = value;
        binder.readBean( this.filmCopy );

    }
}
