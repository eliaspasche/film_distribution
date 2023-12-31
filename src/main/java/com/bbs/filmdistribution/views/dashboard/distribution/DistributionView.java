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
import com.bbs.filmdistribution.service.pdf.InvoicePdfService;
import com.bbs.filmdistribution.service.pdf.ReportPdfService;
import com.bbs.filmdistribution.util.DateUtil;
import com.bbs.filmdistribution.util.MenuBarUtil;
import com.bbs.filmdistribution.util.NotificationUtil;
import com.bbs.filmdistribution.util.NumbersUtil;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
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
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A view to manage the {@link FilmDistribution} objects.
 */
@PageTitle( "Distributions" )
@Route( value = "distribution/:distributionID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
@Uses( Icon.class )
public class DistributionView extends MasterDetailGridLayout<FilmDistribution, FilmDistributionService>
{

    // Route
    private static final String DISTRIBUTION_ID = "distributionID";
    private static final String DISTRIBUTION_EDIT_ROUTE_TEMPLATE = DashboardLayout.DASHBOARD_PATH + "/distribution/%s/edit";

    // Services
    private final CustomerService customerService;
    private final FilmCopyService filmCopyService;
    private final InvoicePdfService invoicePdfService;
    private final Button saveButton = new Button( "Save" );
    private final Filters filters;
    // Layout
    private H3 splitTitle;
    private ComboBox<Customer> customer;
    private MultiSelectComboBox<FilmCopy> filmCopies;
    private DatePicker startDate;
    private DatePicker endDate;

    /**
     * Constructor.
     *
     * @param distributionService The {@link FilmDistributionService}
     * @param customerService     The {@link CustomerService}
     * @param filmCopyService     The {@link FilmCopyService}
     */
    public DistributionView( FilmDistributionService distributionService, CustomerService customerService, FilmCopyService filmCopyService, FilmService filmService, InvoicePdfService invoicePdfService, ReportPdfService reportPdfService )
    {
        super( DISTRIBUTION_ID, DISTRIBUTION_EDIT_ROUTE_TEMPLATE, distributionService );
        this.customerService = customerService;
        this.filmCopyService = filmCopyService;
        this.invoicePdfService = invoicePdfService;

        getHeaderDiv().addClassNames( "distribution-view" );
        filters = new Filters( this::refreshGrid, customerService, filmService, reportPdfService );
        getHeaderDiv().setWidthFull();
        getHeaderDiv().add( createMobileFilters(), filters );

        setCreateButton( new Button( "New " + getEditItemName() ) );
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

        grid.addColumn( item -> NumbersUtil.createLeadingZeroCustomerNumber( item.getId() ) ).setHeader( "Distribution ID" ).setAutoWidth( true );
        grid.addColumn( item -> item.getCustomer().getFullName() ).setHeader( "Customer" ).setAutoWidth( true );
        grid.addColumn( item -> DateUtil.formatDate( item.getStartDate() ), "startDate" ).setHeader( "Start Date" ).setSortable( true ).setAutoWidth( true );
        grid.addColumn( item -> DateUtil.formatDate( item.getEndDate() ), "endDate" ).setHeader( "End Date" ).setSortable( true ).setAutoWidth( true );

        grid.setDetailsVisibleOnClick( false );
        grid.setItemDetailsRenderer( createFilmCopyDetailsRenderer() );

        grid.addComponentColumn( item -> buildMenuBar( item, item.getId().toString(), this ) ).setFrozenToEnd( true );

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
    public void buildMenuBarItems( MenuBar menuBar, FilmDistribution item )
    {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate( true );
        progressBar.setVisible( false );

        MenuItem openDetailsItem = MenuBarUtil.createIconItem( menuBar, VaadinIcon.LIST, "Open film copies", null );
        openDetailsItem.addClickListener( click -> getGrid().setDetailsVisible( item, !getGrid().isDetailsVisible( item ) ) );

        MenuItem downloadMenuItem = MenuBarUtil.createIconItem( menuBar, VaadinIcon.DOWNLOAD, "Download Invoice", null );
        downloadMenuItem.getElement().appendChild( progressBar.getElement() );
        downloadMenuItem.addClickListener( click -> {
            UI ui = click.getSource().getUI().orElseThrow();
            downloadMenuItem.setEnabled( false );
            progressBar.setVisible( true );

            new Thread( () -> ui.access( () -> {
                invoicePdfService.createInvoicePdf( item );
                downloadMenuItem.setEnabled( true );
                progressBar.setVisible( false );
            } ) ).start();
        } );
        downloadMenuItem.getElement().appendChild( progressBar.getElement() );
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
            boolean isNewItem = getItemToEdit() == null;
            if ( isNewItem )
            {
                setItemToEdit( new FilmDistribution() );
            }

            if ( canDistributeFilms( isNewItem ) )
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
    private boolean canDistributeFilms( boolean isNewItem )
    {
        try
        {
            // Check before write bean (edit mode)
            Set<FilmCopy> currentFilmCopies = isNewItem ? new HashSet<>() : getItemToEdit().getFilmCopies();
            getBinder().writeBean( getItemToEdit() );

            FilmDistribution filmDistribution = getItemToEdit();

            if ( !filmsToDistributeAvailable( filmDistribution, currentFilmCopies ) )
            {
                getItemToEdit().setFilmCopies( currentFilmCopies );
                return false;
            }

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

    /**
     * Check if the films to distribute already available.
     *
     * @param filmDistribution  The {@link FilmDistribution}
     * @param currentFilmCopies The current distributed film copies.
     * @return Are the films already available
     */
    private boolean filmsToDistributeAvailable( FilmDistribution filmDistribution, Set<FilmCopy> currentFilmCopies )
    {
        List<FilmCopy> filmCopyList = filmCopyService.getAvailableCopies( filmDistribution.getStartDate(), filmDistribution.getEndDate() );

        if ( currentFilmCopies != null )
        {
            filmCopyList.addAll( currentFilmCopies );
        }

        List<FilmCopy> notAvailable = filmDistribution.getFilmCopies().stream().filter( filmCopy -> filmCopyList.stream().noneMatch( i -> Objects.equals( i.getId(), filmCopy.getId() ) ) ).toList();

        if ( !notAvailable.isEmpty() )
        {
            notAvailable.forEach( item -> NotificationUtil.sendErrorNotification( "The film copy " + item.getFilm().getName() + " is not available until " + DateUtil.formatDate( filmDistribution.getEndDate() ), 5 ) );
            refreshGrid();
            return false;
        }

        return true;
    }

    @Override
    protected void createEditorLayout()
    {
        LocalDate now = DateUtil.now();

        FormLayout formLayout = new FormLayout();

        splitTitle = new H3( "New " + getEditItemName() );

        customer = new ComboBox<>();
        customer.setLabel( "Customer" );
        customer.setItems( customerService.list( Pageable.unpaged() ).stream().toList() );
        customer.setItemLabelGenerator( Customer::getFullName );

        filmCopies = new MultiSelectComboBox<>( "Film Copies" );
        filmCopies.setRenderer( createFilmCopyRenderer() );
        filmCopies.setItems( filmCopyService.getAvailableCopies( now, now.plusDays( 90 ) ) );
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

            updateFilmCopySelection( event.getValue(), endDate.getValue() != null ? endDate.getValue() : endDate.getMax() );
        } );

        endDate.addValueChangeListener( event -> {
            if ( event.getValue() == null )
            {
                return;
            }

            updateFilmCopySelection( startDate.getValue() != null ? startDate.getValue() : now, event.getValue() );
        } );

        startDate.setValue( now );

        formLayout.add( customer, filmCopies, startDate, endDate );
        getEditorDiv().add( splitTitle, formLayout );

        createButtonLayout();
    }

    /**
     * Update the items in the {@link MultiSelectComboBox} with the current selection of start and end date.
     *
     * @param start The start date
     * @param end   The end date
     */
    private void updateFilmCopySelection( LocalDate start, LocalDate end )
    {
        List<FilmCopy> filmCopyList = filmCopyService.getAvailableCopies( start, end );
        Set<FilmCopy> selected = filmCopyList.stream().filter( i -> filmCopies.getSelectedItems().stream().anyMatch( c -> Objects.equals( c.getId(), i.getId() ) ) ).collect( Collectors.toSet() );
        filmCopies.setItems( filmCopyList );
        filmCopies.setValue( selected );
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
            LocalDate start = value != null ? value.getStartDate() : DateUtil.now();
            filmCopyList.addAll( filmCopyService.getAvailableCopies( start, endDate != null ? endDate.getValue() : start.plusDays( 90 ) ) );
            filmCopyList.addAll( getItemToEdit().getFilmCopies() );
            filmCopies.setItems( filmCopyList );
            filmCopies.setValue( getItemToEdit().getFilmCopies() );
        }
    }

    // Grid detail layout for the film copies of a distribution
    @Override
    protected void refreshGrid()
    {
        super.refreshGrid();
        if ( filmCopies != null )
        {
            LocalDate start = DateUtil.now();
            filmCopies.setItems( filmCopyService.getAvailableCopies( start, endDate != null ? endDate.getValue() : start.plusDays( 90 ) ) );
        }
    }

    /**
     * Create the mobile view for the {@link Filters}
     *
     * @return The optimized layout for mobile.
     */
    private HorizontalLayout createMobileFilters()
    {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames( LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER, LumoUtility.AlignItems.CENTER );
        mobileFilters.addClassName( "mobile-filters" );

        Icon mobileIcon = new Icon( "lumo", "plus" );
        Span filtersHeading = new Span( "Filters" );
        mobileFilters.add( mobileIcon, filtersHeading );
        mobileFilters.setFlexGrow( 1, filtersHeading );
        mobileFilters.addClickListener( e -> {
            if ( filters.getClassNames().contains( "visible" ) )
            {
                filters.removeClassName( "visible" );
                mobileIcon.getElement().setAttribute( "icon", "lumo:plus" );
            }
            else
            {
                filters.addClassName( "visible" );
                mobileIcon.getElement().setAttribute( "icon", "lumo:minus" );
            }
        } );
        return mobileFilters;
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

            filmDistribution.getFilmCopies().forEach( item -> {
                MessageListItem filmListItem = new MessageListItem();
                filmListItem.setUserName( item.getFilm().getName() );
                filmListItem.setText( item.getInventoryNumber() );
                filmListItem.setUserAbbreviation( String.valueOf( item.getFilm().getAgeGroup().getMinimumAge() ) );
                filmListItem.setUserColorIndex( Math.toIntExact( item.getFilm().getAgeGroup().getId() ) );
                films.add( filmListItem );
            } );

            filmList.setItems( films );

            add( detailTitle, filmList );
        }
    }

    /**
     * Filter Class for the Distribution View
     */
    public static class Filters extends Div implements Specification<FilmDistribution>
    {
        private final ComboBox<Customer> customer = new ComboBox<>();
        private final ComboBox<Film> film = new ComboBox<>();
        private final DatePicker date = new DatePicker( "Reporting Date" );


        /**
         * Constructor with dependency injection and creation of the filter view.
         */
        public Filters( Runnable onSearch, CustomerService customerService, FilmService filmService, ReportPdfService reportPdfService )
        {
            setWidthFull();
            addClassNames( LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM, LumoUtility.BoxSizing.BORDER );
            addClassName( "filter-layout" );


            customer.setLabel( "Customer" );
            customer.setItems( customerService.list( Pageable.unpaged() ).stream().toList() );
            customer.setItemLabelGenerator( Customer::getFullName );
            customer.setPlaceholder( "Select Customers" );
            customer.setClearButtonVisible( true );

            film.setLabel( "Film" );
            film.setItems( filmService.list( Pageable.unpaged() ).stream().toList() );
            film.setItemLabelGenerator( Film::getName );
            film.setPlaceholder( "Select a Film" );
            film.setClearButtonVisible( true );

            date.setPlaceholder( "Select a Reporting Date" );
            date.setAriaLabel( "Reporting Date" );
            date.setClearButtonVisible( true );


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

            Button exportButton = new Button( "Export Report" );
            exportButton.addThemeVariants( ButtonVariant.LUMO_ICON );
            exportButton.setIcon( new Icon( VaadinIcon.DOWNLOAD ) );

            ProgressBar progressBar = new ProgressBar();
            progressBar.setIndeterminate( true );
            progressBar.setWidth( "15px" );

            exportButton.addClickListener( click -> {

                UI ui = click.getSource().getUI().orElseThrow();
                exportButton.setEnabled( false );
                exportButton.setIcon( progressBar );

                new Thread( () -> ui.access( () -> {
                    reportPdfService.createReportPdf( customer.getValue(), film.getValue(), date.getValue() );
                    exportButton.setEnabled( true );
                    exportButton.setIcon( new Icon( VaadinIcon.DOWNLOAD ) );
                } ) ).start();

                onSearch.run();
            } );

            Div actions = new Div( resetBtn, searchBtn, exportButton );
            actions.addClassName( LumoUtility.Gap.SMALL );
            actions.addClassName( "actions" );

            add( customer, film, date, actions );
        }

        @Override
        public Predicate toPredicate( Root<FilmDistribution> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder )
        {
            List<Predicate> predicates = new ArrayList<>();

            // Add filter predicates
            if ( !customer.isEmpty() )
            {
                predicates.add( criteriaBuilder.equal( root.get( "customer" ), criteriaBuilder.literal( customer.getValue() ) ) );
            }

            if ( !film.isEmpty() )
            {
                predicates.add( criteriaBuilder.equal( root.get( "filmCopies" ).get( "film" ), criteriaBuilder.literal( film.getValue() ) ) );
            }

            if ( !date.isEmpty() )
            {
                predicates.add( criteriaBuilder.greaterThanOrEqualTo( root.get( "endDate" ), criteriaBuilder.literal( date.getValue() ) ) );
                predicates.add( criteriaBuilder.lessThanOrEqualTo( root.get( "startDate" ), criteriaBuilder.literal( date.getValue() ) ) );
            }

            return criteriaBuilder.and( predicates.toArray( Predicate[]::new ) );
        }
    }

}
