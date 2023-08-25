package com.bbs.filmdistribution.views.dashboard.customers;

import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle( "Customers" )
@Route( value = "customers/:customerID?/:action?(edit)", layout = DashboardLayout.class )
@PermitAll
public class CustomersView extends Composite<VerticalLayout>
{

    public CustomersView()
    {
        getContent().setHeightFull();
        getContent().setWidthFull();
    }
}
