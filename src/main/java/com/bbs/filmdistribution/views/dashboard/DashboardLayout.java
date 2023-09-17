package com.bbs.filmdistribution.views.dashboard;

import com.bbs.filmdistribution.config.AppConfig;
import com.bbs.filmdistribution.data.entity.User;
import com.bbs.filmdistribution.security.AuthenticatedUser;
import com.bbs.filmdistribution.service.DarkModeService;
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
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * The main view of the dashboard.
 * This is the top-level url for other views. (/dashboard/<view-child>)
 */
@RoutePrefix( value = DashboardLayout.DASHBOARD_PATH, absolute = true )
@RequiredArgsConstructor
public class DashboardLayout extends AppLayout
{

    public static final String DASHBOARD_PATH = "dashboard";

    private H2 viewTitle;

    private final AppConfig appConfig;
    private final DarkModeService darkModeService;
    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;

    /**
     * Initialization of the components in the current view.
     */
    @PostConstruct
    public void init()
    {
        setPrimarySection( Section.DRAWER );
        addDrawerContent();
        addHeaderContent();
    }

    /**
     * Create the header content of the dashboard.
     */
    private void addHeaderContent()
    {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel( "Menu toggle" );

        viewTitle = new H2();
        viewTitle.addClassNames( LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE );

        addToNavbar( true, toggle, viewTitle, createNavBar() );
    }

    /**
     * Create the content for the sidebar.
     */
    private void addDrawerContent()
    {
        Image image = new Image( "/images/application-logo.png", "Application img" );
        image.getElement().setAttribute( "draggable", "false" );
        image.setClassName( "applicationLogo" );

        Header header = new Header( image );

        Scroller scroller = new Scroller( createNavigation() );

        addToDrawer( header, scroller, createFooter() );
    }

    /**
     * Create the navigation options for the application in the sidebar.
     *
     * @return The created {@link SideNav}
     */
    private SideNav createNavigation()
    {
        SideNav nav = new SideNav();

        Arrays.stream( DashboardViewEnum.values() )
                .filter( i -> accessChecker.hasAccess( i.getPageClass() ) )
                .map( i -> new SideNavItem( i.getPageTitle(), i.getPageClass(), i.getDisplayIcon().create() ) )
                .forEach( nav::addItem );

        return nav;
    }

    /**
     * Create the navigation bar for the dashboard.
     *
     * @return The navigation layout.
     */
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

    /**
     * Create the theme switch {@link Button}
     *
     * @return The created {@link Button}
     */
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

    /**
     * Get the current {@link Icon} of the current theme
     *
     * @return The {@link Icon}
     */
    private Icon getDarkModeIcon()
    {
        return new Icon( darkModeService.isDarkModeActive() ? VaadinIcon.SUN_O : VaadinIcon.MOON_O );
    }

    /**
     * Create the footer content of the dashboard
     *
     * @return The {@link Footer}
     */
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

    /**
     * Get the title of the current dashboard view.
     *
     * @return The page title.
     */
    private String getCurrentPageTitle()
    {
        PageTitle title = getContent().getClass().getAnnotation( PageTitle.class );
        return title == null ? "" : title.value();
    }
}
