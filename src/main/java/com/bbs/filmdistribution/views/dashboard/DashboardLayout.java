package com.bbs.filmdistribution.views.dashboard;

import com.bbs.filmdistribution.config.AppConfig;
import com.bbs.filmdistribution.data.entity.User;
import com.bbs.filmdistribution.security.AuthenticatedUser;
import com.bbs.filmdistribution.service.DarkModeService;
import com.bbs.filmdistribution.views.dashboard.account.AccountView;
import com.bbs.filmdistribution.views.dashboard.agegroups.AgeGroupsView;
import com.bbs.filmdistribution.views.dashboard.customers.CustomersView;
import com.bbs.filmdistribution.views.dashboard.distributionoverview.DistributionOverviewView;
import com.bbs.filmdistribution.views.dashboard.filmcopies.FilmCopiesView;
import com.bbs.filmdistribution.views.dashboard.films.FilmsView;
import com.bbs.filmdistribution.views.dashboard.home.HomeView;
import com.bbs.filmdistribution.views.dashboard.newdistribution.NewDistributionView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
@RoutePrefix( value = DashboardLayout.DASHBOARD_PATH, absolute = true )
public class DashboardLayout extends AppLayout
{

    public static final String DASHBOARD_PATH = "dashboard";

    private H2 viewTitle;

    private final AppConfig appConfig;
    private final DarkModeService darkModeService;
    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;

    public DashboardLayout( AppConfig appConfig, DarkModeService darkModeService, AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker )
    {
        this.appConfig = appConfig;
        this.darkModeService = darkModeService;
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection( Section.DRAWER );
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent()
    {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel( "Menu toggle" );

        viewTitle = new H2();
        viewTitle.addClassNames( LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE );

        addToNavbar( true, toggle, viewTitle, createNavBar() );
    }

    private void addDrawerContent()
    {
        H1 appName = new H1( "Film Distribution" );
        appName.addClassNames( LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE );
        Header header = new Header( appName );

        Scroller scroller = new Scroller( createNavigation() );

        addToDrawer( header, scroller, createFooter() );
    }

    private SideNav createNavigation()
    {
        SideNav nav = new SideNav();

        if ( accessChecker.hasAccess( HomeView.class ) )
        {
            nav.addItem( new SideNavItem( "Dashboard", HomeView.class, LineAwesomeIcon.HOME_SOLID.create() ) );

        }
        if ( accessChecker.hasAccess( NewDistributionView.class ) )
        {
            nav.addItem( new SideNavItem( "New Distribution", NewDistributionView.class, LineAwesomeIcon.USER.create() ) );

        }
        if ( accessChecker.hasAccess( DistributionOverviewView.class ) )
        {
            nav.addItem( new SideNavItem( "Distribution Overview", DistributionOverviewView.class, LineAwesomeIcon.RECEIPT_SOLID.create() ) );

        }
        if ( accessChecker.hasAccess( CustomersView.class ) )
        {
            nav.addItem( new SideNavItem( "Customers", CustomersView.class, LineAwesomeIcon.USERS_SOLID.create() ) );

        }
        if ( accessChecker.hasAccess( FilmsView.class ) )
        {
            nav.addItem( new SideNavItem( "Films", FilmsView.class, LineAwesomeIcon.FILM_SOLID.create() ) );

        }
        if ( accessChecker.hasAccess( FilmCopiesView.class ) )
        {
            nav.addItem( new SideNavItem( "Film Copies", FilmCopiesView.class, LineAwesomeIcon.COLUMNS_SOLID.create() ) );

        }
        if ( accessChecker.hasAccess( AgeGroupsView.class ) )
        {
            nav.addItem( new SideNavItem( "Age Groups", AgeGroupsView.class, LineAwesomeIcon.CALENDAR_SOLID.create() ) );

        }
        if ( accessChecker.hasAccess( AccountView.class ) )
        {
            nav.addItem( new SideNavItem( "Account", AccountView.class, LineAwesomeIcon.USER.create() ) );

        }

        return nav;
    }

    private HorizontalLayout createNavBar()
    {
        HorizontalLayout navbarLayout = new HorizontalLayout();
        navbarLayout.getStyle().set( "position", "absolute" ).set( "right", "10px" );
        navbarLayout.setJustifyContentMode( FlexComponent.JustifyContentMode.CENTER );

        User user = authenticatedUser.get().orElse( null );
        if ( user == null )
        {
            return navbarLayout;
        }

        Button logout = new Button( "Logout", e -> authenticatedUser.logout() );

        Button darkModeButton = createDarkModeButton();
        navbarLayout.add( logout, darkModeButton );

        if ( user.getDarkMode() == 1 && !darkModeService.isDarkModeActive() )
        {
            user.setDarkMode( darkModeService.changeDarkMode() );
            darkModeButton.setIcon( getDarkModeIcon() );
        }

        return navbarLayout;
    }

    private Button createDarkModeButton()
    {
        Button darkModeButton = new Button( getDarkModeIcon() );
        darkModeButton.addThemeVariants( ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST );

        darkModeButton.addClickListener( click -> {
            authenticatedUser.get().ifPresent( user -> user.setDarkMode( darkModeService.changeDarkMode() ) );
            darkModeButton.setIcon( getDarkModeIcon() );
        } );

        return darkModeButton;
    }

    private Icon getDarkModeIcon()
    {
        return new Icon( darkModeService.isDarkModeActive() ? VaadinIcon.SUN_O : VaadinIcon.MOON_O );
    }

    private Footer createFooter()
    {
        Footer layout = new Footer();

        Paragraph version = new Paragraph( appConfig.getVersion() );
        version.setClassName( "versionText" );
        layout.add( version );
        return layout;
    }

    @Override
    protected void afterNavigation()
    {
        super.afterNavigation();
        viewTitle.setText( getCurrentPageTitle() );
    }

    private String getCurrentPageTitle()
    {
        PageTitle title = getContent().getClass().getAnnotation( PageTitle.class );
        return title == null ? "" : title.value();
    }
}
