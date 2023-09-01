package com.bbs.filmdistribution.views.dashboard.films;

import com.bbs.filmdistribution.data.entity.AgeGroup;
import com.bbs.filmdistribution.data.entity.Film;
import com.bbs.filmdistribution.data.service.AgeGroupService;
import com.bbs.filmdistribution.data.service.FilmService;
import com.bbs.filmdistribution.util.ComponentUtil;
import com.bbs.filmdistribution.util.NotificationUtil;
import com.bbs.filmdistribution.views.DynamicView;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.bbs.filmdistribution.wrapper.EntityDeleteDialog;
import com.bbs.filmdistribution.wrapper.GridFilter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
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

@PageTitle( "Films" )
@Route( value = "films/:filmID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
@RequiredArgsConstructor
public class FilmsView extends Div implements DynamicView, BeforeEnterObserver
{
    // Route
    private static final String FILM_ID = "filmID";
    private static final String FILM_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/films/%s/edit";

    // Services
    private final FilmService filmService;
    private final AgeGroupService ageGroupService;

    // Layout
    private final Grid<Film> grid = new Grid<>( Film.class, false );

    private H3 splitTitle;
    private TextField name;
    private IntegerField length;
    private Select<AgeGroup> ageGroup;
    private NumberField price;

    private final Button create = new Button( "New Film" );
    private final Button save = new Button( "Save" );

    // Validator
    private BeanValidationBinder<Film> binder;

    private Film film;

    @PostConstruct
    public void init()
    {
        addClassNames( "films-view" );

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout( splitLayout );
        createEditorLayout( splitLayout );

        add( splitLayout );

        buildGrid();

        // Configure Form
        binder = new BeanValidationBinder<>( Film.class );

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField( name ).asRequired().bind( "name" );
        binder.forField( length ).asRequired().bind( "length" );
        binder.forField( price ).asRequired().bind( "price" );
        binder.forField( ageGroup ).asRequired().bind( Film::getAgeGroup, Film::setAgeGroup );

        binder.bindInstanceFields( this );

        create.addClickListener( e -> {
            clearForm();
            refreshGrid();
        } );

        save.addClickListener( e -> {
            try
            {
                if ( this.film == null )
                {
                    this.film = new Film();
                }
                binder.writeBean( this.film );
                filmService.update( this.film );
                clearForm();
                refreshGrid();
                NotificationUtil.sendSuccessNotification( "Data updated", 2 );
                UI.getCurrent().navigate( FilmsView.class );
            }
            catch ( ObjectOptimisticLockingFailureException exception )
            {
                NotificationUtil.sendErrorNotification( "Error updating the data. Somebody else has updated the record while you were making changes", 2 );
            }
            catch ( ValidationException validationException )
            {
                NotificationUtil.sendErrorNotification( "Failed to update the data. Check again that all values are valid", 2 );
            }
        } );
    }

    private void buildGrid()
    {
        // Configure Grid
        grid.addThemeVariants( GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER );

        grid.setItems( query -> filmService.list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ) ).stream() );

        Grid.Column<Film> filmNameColumn = grid.addColumn( "name" ).setAutoWidth( true );
        grid.addColumn( "length" ).setAutoWidth( true );
        grid.addColumn( item -> item.getAgeGroup().getName() ).setHeader( "Age Group" ).setAutoWidth( true );
        grid.addColumn( "price" ).setAutoWidth( true );
        grid.addColumn( item -> filmService.availableCopies( item.getId() ) ).setHeader( "Copies" ).setAutoWidth( true );
        grid.addComponentColumn( item -> {
            Button deleteButton = new Button( "Delete" );
            deleteButton.setTooltipText( "Shift + Click = Instant delete" );
            deleteButton.addThemeVariants( ButtonVariant.LUMO_ERROR );
            deleteButton.addClickListener( e -> {
                if ( e.isShiftKey() )
                {
                    filmService.delete( item.getId() );
                    refreshGrid();
                    return;
                }
                new EntityDeleteDialog<>( "Should the film \"" + item.getName() + "\" removed?", filmService, item, this );
            } );

            return deleteButton;
        } );

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener( event -> {
            if ( event.getValue() != null )
            {
                UI.getCurrent().navigate( String.format( FILM_EDIT_ROUTE_TEMPLATE, event.getValue().getId() ) );
            }
            else
            {
                clearForm();
                UI.getCurrent().navigate( FilmsView.class );
            }
        } );

        // Create search filter
        HeaderRow headerRow = grid.appendHeaderRow();

        GridFilter<Film> gridFilter = new GridFilter<>( grid, filmService );

        TextField filterTextField = ComponentUtil.createGridSearchField( "Search" );
        filterTextField.setValueChangeMode( ValueChangeMode.LAZY );
        filterTextField.addValueChangeListener( e -> gridFilter.filterFieldName( "name", e.getValue() ) );

        headerRow.getCell( filmNameColumn ).setComponent( filterTextField );
    }

    @Override
    public void beforeEnter( BeforeEnterEvent event )
    {
        Optional<Long> filmId = event.getRouteParameters().get( FILM_ID ).map( Long::parseLong );
        create.setVisible( filmId.isPresent() );
        if ( filmId.isPresent() )
        {
            Optional<Film> filmFromBackend = filmService.get( filmId.get() );
            if ( filmFromBackend.isPresent() )
            {
                populateForm( filmFromBackend.get() );
            }
            else
            {
                Notification.show( String.format( "The requested film was not found, ID = %s", filmId.get() ), 3000, Notification.Position.BOTTOM_START );
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo( FilmsView.class );
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

        splitTitle = new H3( "New film" );
        name = new TextField( "Name" );
        length = ComponentUtil.createIntegerField( "Length", "sec." );
        ageGroup = new Select<>();
        ageGroup.setLabel( "Age Group" );
        ageGroup.setEmptySelectionAllowed( false );
        ageGroup.setItems( ageGroupService.list( Pageable.unpaged() ).stream().toList() );
        ageGroup.setItemLabelGenerator( AgeGroup::getName );

        price = ComponentUtil.createNumberField( "Price", "€/week" );
        price.setMax( 99.99 );

        formLayout.add( name, length, ageGroup, price );

        editorDiv.add( splitTitle, formLayout );
        createButtonLayout( editorLayoutDiv );

        splitLayout.addToSecondary( editorLayoutDiv );
    }

    private void createButtonLayout( Div editorLayoutDiv )
    {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName( "button-layout" );
        create.addThemeVariants( ButtonVariant.LUMO_TERTIARY );
        save.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

        buttonLayout.add( save, create );
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

    private void populateForm( Film value )
    {
        this.film = value;
        binder.readBean( this.film );
        splitTitle.setText( ( this.film == null ? "New" : "Edit" ) + " film" );
    }

    @Override
    public void updateView()
    {
        refreshGrid();
    }

}
