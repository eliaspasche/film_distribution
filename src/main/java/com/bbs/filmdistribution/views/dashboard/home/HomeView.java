package com.bbs.filmdistribution.views.dashboard.home;

import com.bbs.filmdistribution.components.ExampleCard;
import com.bbs.filmdistribution.event.ThemeVariantChangedEvent;
import com.bbs.filmdistribution.service.DarkModeService;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.datalables.builder.StyleBuilder;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.theme.Mode;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;


@PageTitle( "Home" )
@Route( value = "home", layout = DashboardLayout.class )
@RouteAlias( value = "", layout = DashboardLayout.class )
@PermitAll
public class HomeView extends VerticalLayout
{
    private final Div chartLayout = new Div();

    private final DarkModeService darkModeService;

    public HomeView( DarkModeService darkModeService )
    {
        this.darkModeService = darkModeService;

        buildLayout();
    }


    private void buildLayout()
    {
        setSpacing( false );

        chartLayout.setClassName( "chartLayout" );
        chartLayout.add( buildChart() );
        add( new ExampleCard( "The title", "description" ), chartLayout );

        ThemeVariantChangedEvent.addThemeChangedListener( UI.getCurrent(), e -> {
            chartLayout.removeAll();
            chartLayout.add( buildChart() );
        } );

        setSizeFull();
        setJustifyContentMode( JustifyContentMode.CENTER );
        setDefaultHorizontalComponentAlignment( Alignment.CENTER );
        getStyle().set( "text-align", "center" );

    }

    private ApexCharts buildChart()
    {

        ApexChartsBuilder apexChartsBuilder = new ApexChartsBuilder();
        apexChartsBuilder.withTheme( ThemeBuilder.get()
                .withMode( darkModeService.isDarkModeActive() ? Mode.DARK : Mode.LIGHT ).build() );

        // transparent is a valid color
        apexChartsBuilder.withChart( ChartBuilder.get().withType( Type.BAR ).withBackground( "transparent" ).build() ).withPlotOptions(
                        PlotOptionsBuilder.get().withBar(
                                BarBuilder.get().withColors( null ).withHorizontal( false ).withDistributed( true )
                                        .withColumnWidth( "55%" ).build() ).build() ).withDataLabels(
                        DataLabelsBuilder.get().withEnabled( true )
                                .withStyle( StyleBuilder.get().build() )
                                .build() ).withStroke(
                        StrokeBuilder.get().withShow( true ).withWidth( 1.0 ).withColors( "var(--lumo-contrast)" ).build() )
                .withSeries( new Series<>( "Net Profit", "44", "55", "57", "56", "61", "58", "63", "60", "66" ),
                        new Series<>( "Revenue", "76", "85", "101", "98", "87", "105", "91", "114", "94" ),
                        new Series<>( "Free Cash Flow", "35", "41", "36", "26", "45", "48", "52", "53", "41" ) )
                .withXaxis( XAxisBuilder.get().withCategories( "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct" ).build() );

        return apexChartsBuilder.build();
    }
}
