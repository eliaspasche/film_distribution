package com.bbs.filmdistribution.views.dashboard.home;

import com.bbs.filmdistribution.common.TopFilmDistributionDTO;
import com.bbs.filmdistribution.components.HomeLayout;
import com.bbs.filmdistribution.components.InfoCard;
import com.bbs.filmdistribution.data.service.CustomerService;
import com.bbs.filmdistribution.data.service.FilmCopyService;
import com.bbs.filmdistribution.data.service.FilmDistributionService;
import com.bbs.filmdistribution.data.service.FilmService;
import com.bbs.filmdistribution.event.ThemeVariantChangedEvent;
import com.bbs.filmdistribution.service.DarkModeService;
import com.bbs.filmdistribution.views.dashboard.DashboardLayout;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.datalables.builder.StyleBuilder;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.theme.Mode;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * The destination page of the application after login.
 * Some useful information about the saved data is displayed here.
 */
@PageTitle( "Home" )
@Route( value = "home", layout = DashboardLayout.class )
@RouteAlias( value = "", layout = DashboardLayout.class )
@PermitAll
@RequiredArgsConstructor
public class HomeView extends HomeLayout
{
    // Services
    private final DarkModeService darkModeService;
    private final CustomerService customerService;
    private final FilmService filmService;
    private final FilmCopyService filmCopyService;
    private final FilmDistributionService filmDistributionService;

    @PostConstruct
    public void init()
    {
        buildLayout();
    }

    /**
     * Build the layout with {@link InfoCard} and {@link ApexCharts}
     */
    private void buildLayout()
    {
        getInfoLayout().add( new InfoCard( "" + customerService.count(), "Customers", true ) );
        getInfoLayout().add( new InfoCard( "" + filmService.count(), "Films", true ) );
        getInfoLayout().add( new InfoCard( "" + filmCopyService.count(), "Film Copies", true ) );
        getInfoLayout().add( new InfoCard( "" + filmDistributionService.count(), "Distributions", true ) );
        getLayout().add( buildTopFilmDistributionChart(), buildPieChart() );

        ThemeVariantChangedEvent.addThemeChangedListener( UI.getCurrent(), e -> {
            getLayout().removeAll();
            getLayout().add( buildTopFilmDistributionChart(), buildPieChart() );
        } );

    }

    /**
     * Create the chart for the top film distribution in the application.
     *
     * @return The {@link ApexCharts}
     */
    private ApexCharts buildTopFilmDistributionChart()
    {
        int displayAmount = 5;

        ApexChartsBuilder apexChartsBuilder = new ApexChartsBuilder();
        apexChartsBuilder.withTheme( ThemeBuilder.get()
                .withMode( darkModeService.isDarkModeActive() ? Mode.DARK : Mode.LIGHT ).build() );

        List<TopFilmDistributionDTO> topFilmDistributions = filmDistributionService.getTopFilmDistributions( displayAmount );

        Long[] dataValues = new Long[ topFilmDistributions.size() ];
        String[] filmNames = new String[ topFilmDistributions.size() ];
        for ( int i = 0; i < dataValues.length; i++ )
        {
            TopFilmDistributionDTO topFilmDistributionDTO = topFilmDistributions.get( i );
            dataValues[ i ] = topFilmDistributionDTO.getCurrentDistributionAmount();
            filmNames[ i ] = topFilmDistributionDTO.getFilmName();
        }

        apexChartsBuilder.withChart( ChartBuilder.get().withType( Type.BAR ).withBackground( "transparent" ).build() ).withPlotOptions(
                        PlotOptionsBuilder.get().withBar(
                                BarBuilder.get().withColors( null ).withHorizontal( false ).withDistributed( true )
                                        .withColumnWidth( "55%" ).build() ).build() ).withDataLabels(
                        DataLabelsBuilder.get().withEnabled( true )
                                .withStyle( StyleBuilder.get().build() )
                                .build() ).withStroke(
                        StrokeBuilder.get().withShow( true ).withWidth( 1.0 ).withColors( "var(--lumo-contrast)" ).build() )
                .withSeries( new Series<>( "Amount", dataValues ) )
                .withXaxis( XAxisBuilder.get().withCategories( filmNames ).build() )
                .withTitle( TitleSubtitleBuilder.get().withText( "Top " + displayAmount + " film distributions" ).build() );

        ApexCharts apexCharts = apexChartsBuilder.build();
        apexCharts.setClassName( "chartLayout" );

        return apexCharts;
    }

    private ApexCharts buildPieChart()
    {
        ApexChartsBuilder apexChartsBuilder = new ApexChartsBuilder();
        apexChartsBuilder.withTheme( ThemeBuilder.get()
                .withMode( darkModeService.isDarkModeActive() ? Mode.DARK : Mode.LIGHT ).build() );

        apexChartsBuilder.withChart( ChartBuilder.get().withType( Type.DONUT ).withBackground( "transparent" ).build() )
                .withLegend( LegendBuilder.get()
                        .withPosition( Position.BOTTOM )
                        .build() )
                .withSeries( 44.0, 55.0, 41.0, 17.0, 15.0 )
                .withTitle( TitleSubtitleBuilder.get().withText( "Example chart" ).build() );

        ApexCharts apexCharts = apexChartsBuilder.build();
        apexCharts.setClassName( "chartLayout" );

        return apexCharts;
    }
}
