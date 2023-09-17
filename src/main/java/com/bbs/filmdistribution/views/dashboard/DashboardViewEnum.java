package com.bbs.filmdistribution.views.dashboard;

import com.bbs.filmdistribution.views.dashboard.account.AccountView;
import com.bbs.filmdistribution.views.dashboard.agegroups.AgeGroupsView;
import com.bbs.filmdistribution.views.dashboard.customers.CustomersView;
import com.bbs.filmdistribution.views.dashboard.distribution.DistributionView;
import com.bbs.filmdistribution.views.dashboard.filmcopies.FilmCopiesView;
import com.bbs.filmdistribution.views.dashboard.films.FilmsView;
import com.bbs.filmdistribution.views.dashboard.home.HomeView;
import com.vaadin.flow.component.Component;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * Available dashboard pages with title and specific {@link LineAwesomeIcon} (displayed in sidebar navigation).
 * The order in this class corresponds to the order in the sidebar.
 */
@RequiredArgsConstructor
@Getter
public enum DashboardViewEnum
{

    HOME( "Dashboard", HomeView.class, LineAwesomeIcon.HOME_SOLID ),
    DISTRIBUTIONS( "Distributions", DistributionView.class, LineAwesomeIcon.USER ),
    CUSTOMERS( "Customers", CustomersView.class, LineAwesomeIcon.USERS_SOLID ),
    FILMS( "Films", FilmsView.class, LineAwesomeIcon.FILM_SOLID ),
    FILM_COPIES( "Film Copies", FilmCopiesView.class, LineAwesomeIcon.COLUMNS_SOLID ),
    AGE_GROUPS( "Age Groups", AgeGroupsView.class, LineAwesomeIcon.CALENDAR_SOLID ),
    ACCOUNT( "Account", AccountView.class, LineAwesomeIcon.USER );

    private final String pageTitle;
    private final Class<? extends Component> pageClass;
    private final LineAwesomeIcon displayIcon;

}
