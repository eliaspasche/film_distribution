package com.bbs.filmdistribution.views.dashboard.distribution;

import com.bbs.filmdistribution.components.MasterDetailGridLayout;
import com.bbs.filmdistribution.data.entity.Customer;
import com.bbs.filmdistribution.data.entity.Film;
import com.bbs.filmdistribution.data.entity.FilmCopy;
import com.bbs.filmdistribution.data.entity.FilmDistribution;
import com.bbs.filmdistribution.data.service.CustomerService;
import com.bbs.filmdistribution.data.service.FilmCopyService;
import com.bbs.filmdistribution.data.service.FilmDistributionService;
import com.bbs.filmdistribution.data.service.FilmService;
import com.bbs.filmdistribution.util.CustomerNumberUtil;
import com.bbs.filmdistribution.util.DateUtil;
import com.bbs.filmdistribution.util.NotificationUtil;
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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
public class DistributionView extends MasterDetailGridLayout<FilmDistribution, FilmDistributionService> implements DynamicView
{

    // Route
    private static final String DISTRIBUTION_ID = "distributionID";
    private static final String DISTRIBUTION_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/distribution/%s/edit";

    // Services
    private final CustomerService customerService;
    private final FilmCopyService filmCopyService;
    private final FilmService filmService;
    private final Button saveButton = new Button( "Save" );
    private final Filters filters;
    // Layout
    private H3 splitTitle;
    private Select<Customer> customer;
    private MultiSelectComboBox<FilmCopy> filmCopies;
    private DatePicker startDate;
    private DatePicker endDate;

    /**
     * The constructor.
     *
     * @param distributionService The {@link FilmDistributionService}
     * @param customerService     The {@link CustomerService}
     * @param filmCopyService     The {@link FilmCopyService}
     */
    public DistributionView( FilmDistributionService distributionService, CustomerService customerService, FilmCopyService filmCopyService, FilmService filmService )
    {
        super( DISTRIBUTION_ID, DISTRIBUTION_EDIT_ROUTE_TEMPLATE, distributionService );
        this.customerService = customerService;
        this.filmCopyService = filmCopyService;
        this.filmService = filmService;


        getHeaderDiv().addClassNames( "distribution-view" );
        filters = new Filters( this::refreshGrid, customerService, filmService );
        getHeaderDiv().setWidthFull();
        getHeaderDiv().add( filters );

        setCreateButton( new Button( "New " + getEditItemName() ) );
    }

    /**
     * Create the button to toggle or view the film copies.
     *
     * @param grid The {@link Grid}
     * @return The {@link Renderer} for a {@link FilmDistribution}
     */
    private static Renderer<FilmDistribution> createToggleDetailsRenderer( Grid<FilmDistribution> grid )
    {
        return LitRenderer.<FilmDistribution> of( "<vaadin-button theme=\"tertiary\" @click=\"${handleClick}\">Film copies</vaadin-button>" ).withFunction( "handleClick", item -> grid.setDetailsVisible( item, !grid.isDetailsVisible( item ) ) );
    }

    /**
     * Create the {@link ComponentRenderer} to display details for a {@link FilmDistribution}
     *
     * @return The {@link ComponentRenderer}
     */
    private static ComponentRenderer<FilmCopyDetailsLayout, FilmDistribution> createFilmCopyDetailsRenderer()
    {
        return new ComponentRenderer<>( FilmCopyDetailsLayout::new, FilmCopyDetailsLayout::setFilmCopyList );
    }

    @Override
    protected void buildGrid()
    {
        // Configure Grid
        Grid<FilmDistribution> grid = new Grid<>( FilmDistribution.class, false );
        setGrid( grid );

        grid.addThemeVariants( GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER );

        grid.setItems( query -> getDatabaseService().list( PageRequest.of( query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort( query ) ), filters ).stream() );

        grid.addColumn( item -> CustomerNumberUtil.createLeadingZeroCustomerNumber( item.getId() ) ).setHeader( "ID" ).setAutoWidth( true );
        grid.addColumn( item -> item.getCustomer().getFirstName() + " " + item.getCustomer().getName() ).setHeader( "Customer" ).setAutoWidth( true );
        grid.addColumn( item -> DateUtil.formatDate( item.getStartDate() ), "startDate" ).setHeader( "Start-Date" ).setSortable( true ).setAutoWidth( true );
        grid.addColumn( item -> DateUtil.formatDate( item.getEndDate() ), "endDate" ).setHeader( "End-Date" ).setSortable( true ).setAutoWidth( true );
        grid.addColumn( createToggleDetailsRenderer( grid ) );

        grid.setDetailsVisibleOnClick( false );
        grid.setItemDetailsRenderer( createFilmCopyDetailsRenderer() );

        grid.addComponentColumn( item -> getDeleteButton( item, item.getId().toString(), this ) ).setAutoWidth( true ).setFrozenToEnd( true );

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

            if ( canDistributeFilms() )
            {
                saveItem();
            }
        } );

    }

    /**
     * Check the list of {@link FilmCopy} for AgeGroup to a {@link Customer}
     *
     * @return {@link Customer} can distribute
     */
    private boolean canDistributeFilms()
    {
        try
        {
            getBinder().writeBean( getItemToEdit() );

            FilmDistribution filmDistribution = getItemToEdit();

            int customerAge = DateUtil.getAgeByDate( filmDistribution.getCustomer().getDateOfBirth() );

            List<FilmCopy> notAllowedFilms = filmDistribution.getFilmCopies().stream().filter( item -> item.getFilm().getAgeGroup().getMinimumAge() > customerAge ).toList();

            if ( notAllowedFilms.isEmpty() )
            {
                return true;
            }

            notAllowedFilms.forEach( item -> NotificationUtil.sendErrorNotification( "The customer is not old enough for " + item.getFilm().getName(), 3 ) );
            return false;
        }
        catch ( ValidationException validationException )
        {
            NotificationUtil.sendErrorNotification( "Failed to update the data. Check again that all values are valid", 2 );
        }
        return false;
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
        filmCopies.setRenderer( createFilmCopyRenderer() );
        filmCopies.setItems( filmCopyService.getAvailableCopies() );
        filmCopies.setItemLabelGenerator( c -> c.getFilm().getName() + " " + c.getFilm().getAgeGroup().getName() );

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

    /**
     * Create the {@link Renderer} for a {@link FilmCopy}.
     * This is the layout for the dropdown menu of the {@link MultiSelectComboBox}
     *
     * @return The created {@link Renderer}
     */
    private Renderer<FilmCopy> createFilmCopyRenderer()
    {
        StringBuilder htmlStructure = new StringBuilder();
        htmlStructure.append( "<div style=\"display: flex;\">" );
        htmlStructure.append( "  <div>" );
        htmlStructure.append( "    ${item.filmName}" );
        htmlStructure.append( "    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.inventoryId}</div>" );
        htmlStructure.append( "    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.ageGroup}</div>" );
        htmlStructure.append( "  </div>" );
        htmlStructure.append( "</div>" );

        return LitRenderer.<FilmCopy> of( htmlStructure.toString() ).withProperty( "filmName", item -> item.getFilm().getName() ).withProperty( "inventoryId", item -> item.getInventoryNumber().substring( 0, Math.min( item.getInventoryNumber().length(), 25 ) ) + "..." ).withProperty( "ageGroup", item -> item.getFilm().getAgeGroup().getName() );
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

    // Grid detail layout for the film copies of an distribution

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

    /**
     * The layout for the list of {@link FilmCopy} from {@link FilmDistribution}
     */
    private static class FilmCopyDetailsLayout extends Div
    {

        /**
         * Create the layout for the list of {@link FilmCopy} for a {@link FilmDistribution}
         *
         * @param filmDistribution The {@link FilmDistribution}
         */
        public void setFilmCopyList( FilmDistribution filmDistribution )
        {
            H5 detailTitle = new H5( "Film copies" );
            detailTitle.getStyle().set( "margin-bottom", ".5em" );

            MessageList filmList = new MessageList();

            List<MessageListItem> films = new ArrayList<>();

            // TODO: Add color of FSK label?
            filmDistribution.getFilmCopies().forEach( item -> {
                MessageListItem filmListItem = new MessageListItem();
                filmListItem.setUserName( item.getFilm().getName() );
                filmListItem.setText( item.getInventoryNumber() );
                filmListItem.setUserAbbreviation( String.valueOf( item.getFilm().getAgeGroup().getMinimumAge() ) );
                filmListItem.setUserColorIndex( Math.toIntExact( item.getFilm().getAgeGroup().getId() ) );
                films.add( filmListItem );
            } );

            filmList.setItems( films );

            add( detailTitle );
            add( filmList );
        }

        /**
         * Create badge for a text
         *
         * @param text The text.
         * @return The created badge
         */
        private Span createBadge( String text )
        {
            Span badge = new Span( text );
            badge.getElement().getThemeList().add( "badge" );
            badge.getStyle().set( "margin", ".2em" );
            return badge;
        }

    }

    public static class Filters extends Div implements Specification<FilmDistribution>
    {
        private final Select<Customer> customer = new Select<>();
        private final Select<Film> film = new Select<>();
        private final DatePicker date = new DatePicker( "Date" );


        public Filters( Runnable onSearch, CustomerService customerService, FilmService filmService )
        {
            setWidthFull();
            addClassNames( LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM, LumoUtility.BoxSizing.BORDER );
            addClassName( "filter-layout" );


            customer.setLabel( "Customer" );
            customer.setItems( customerService.list( Pageable.unpaged() ).stream().toList() );
            customer.setItemLabelGenerator( Customer::fullName );
            customer.setPlaceholder( "Select a Customer" );

            film.setLabel( "Film" );
            film.setItems( filmService.list( Pageable.unpaged() ).stream().toList() );
            film.setItemLabelGenerator( Film::getName );
            film.setPlaceholder( "Select a Film" );

            date.setPlaceholder( "Select a Date" );
            date.setAriaLabel( "Start Date" );


            // Action buttons
            Button resetBtn = new Button( "Reset" );
            resetBtn.addThemeVariants( ButtonVariant.LUMO_TERTIARY );
            resetBtn.addClickListener( e -> {
                customer.clear();
                film.clear();
                date.clear();
                onSearch.run();
            } );

            Button searchBtn = new Button( "Search" );
            searchBtn.addThemeVariants( ButtonVariant.LUMO_PRIMARY );
            searchBtn.addClickListener( e -> onSearch.run() );

            Div actions = new Div( resetBtn, searchBtn );
            actions.addClassName( LumoUtility.Gap.SMALL );
            actions.addClassName( "actions" );

            add( customer, film, date, actions );
        }

        @Override
        public Predicate toPredicate( Root<FilmDistribution> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder )
        {
            List<Predicate> predicates = new ArrayList<>();

            if ( customer.getValue() != null )
            {
                Customer currentCustomer = customer.getValue();
                Predicate customerMatch = criteriaBuilder.equal( root.get( "customer" ), currentCustomer );
                predicates.add( criteriaBuilder.or( customerMatch ) );

                System.out.println( "id: " + currentCustomer.getId() );
            }
            if ( !film.isEmpty() )
            {
                // TODO: Fix me
                Subquery<Film> filmSubquery = query.subquery( Film.class );
                Root<Film> filmRoot = filmSubquery.from( Film.class );
                filmSubquery.select( filmRoot ).where( criteriaBuilder.equal( filmRoot.get( "name" ), film.getValue().getName() ) );

                Predicate filmMatch = criteriaBuilder.exists( filmSubquery );
                System.out.println( filmMatch );
                predicates.add( filmMatch );

            }
            if ( date.getValue() != null )
            {
                predicates.add( criteriaBuilder.greaterThanOrEqualTo( root.get( "endDate" ), criteriaBuilder.literal( date.getValue() ) ) );
                predicates.add( criteriaBuilder.lessThanOrEqualTo( root.get( "startDate" ), criteriaBuilder.literal( date.getValue() ) ) );
            }
            return criteriaBuilder.and( predicates.toArray( Predicate[]::new ) );
        }
    }

}
