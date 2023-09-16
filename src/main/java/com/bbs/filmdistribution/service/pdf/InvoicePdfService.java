package com.bbs.filmdistribution.service.pdf;

import com.bbs.filmdistribution.common.DistributionInvoiceDTO;
import com.bbs.filmdistribution.config.AppConfig;
import com.bbs.filmdistribution.data.entity.Customer;
import com.bbs.filmdistribution.data.entity.FilmDistribution;
import com.bbs.filmdistribution.data.service.FilmDistributionService;
import com.bbs.filmdistribution.service.FileDownloadService;
import com.bbs.filmdistribution.util.DateUtil;
import com.bbs.filmdistribution.util.NumbersUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Service to create pdf files for invoice.
 */
@Component
public class InvoicePdfService extends AbstractPdfService
{

    private final FilmDistributionService filmDistributionService;

    /**
     * The constructor.
     *
     * @param appConfig               The {@link AppConfig}
     * @param fileDownloadService     The {@link FileDownloadService}
     * @param filmDistributionService The {@link FilmDistributionService}
     */
    public InvoicePdfService( AppConfig appConfig, FileDownloadService fileDownloadService, FilmDistributionService filmDistributionService )
    {
        super( appConfig, fileDownloadService );
        this.filmDistributionService = filmDistributionService;
    }

    /**
     * Create the invoice pdf by a {@link FilmDistribution}.
     *
     * @param filmDistribution The {@link FilmDistribution}
     */
    public void createInvoicePdf( FilmDistribution filmDistribution )
    {
        Customer customer = filmDistribution.getCustomer();
        List<DistributionInvoiceDTO> distributionInvoiceDTOList = filmDistributionService.getDistributionInvoiceByDistribution( filmDistribution.getId() );

        String fileInput = loadPdfTemplate( "film-invoice.html" ).orElse( "" );

        Document htmlDocument = createDocumentFromHtmlText( fileInput );

        // Fill customer data
        String customerNumber = NumbersUtil.createLeadingZeroCustomerNumber( customer.getId() );
        getElementByDocument( htmlDocument, "customerNumber" ).appendText( customerNumber );
        getElementByDocument( htmlDocument, "customerName" ).appendText( customer.getFirstName() + " " + customer.getName() );
        getElementByDocument( htmlDocument, "customerAddress" ).appendText( customer.getAddress() );
        getElementByDocument( htmlDocument, "customerPlz" ).appendText( customer.getZipCode() + " " + customer.getCity() );

        double costsNet = 0;
        Element filmTable = getElementByDocument( htmlDocument, "table" );
        for ( DistributionInvoiceDTO distributionInvoiceDTO : distributionInvoiceDTOList )
        {
            costsNet += distributionInvoiceDTO.getPriceTotal();
            filmTable.append( buildInvoiceEntry( distributionInvoiceDTO ) );
        }

        double tax = costsNet * 0.19;
        getElementByDocument( htmlDocument, "costsNet" ).appendText( NumbersUtil.formatCurrency( costsNet ) );
        getElementByDocument( htmlDocument, "costsText" ).appendText( NumbersUtil.formatCurrency( tax ) );
        getElementByDocument( htmlDocument, "costsAmount" ).appendText( NumbersUtil.formatCurrency( costsNet + tax ) );

        createPdfFile( htmlDocument, NumbersUtil.createLeadingZeroCustomerNumber( filmDistribution.getId() ), "film-invoice.css" );
    }

    /**
     * Build invoice table entry in the pdf.
     *
     * @param distributionInvoiceDTO The {@link DistributionInvoiceDTO}
     * @return Created html text that will be inserted into the table.
     */
    private String buildInvoiceEntry( DistributionInvoiceDTO distributionInvoiceDTO )
    {
        String startDate = DateUtil.formatDate( distributionInvoiceDTO.getStartDate() );
        String endDate = DateUtil.formatDate( distributionInvoiceDTO.getEndDate() );

        StringBuilder invoiceEntry = new StringBuilder();
        invoiceEntry.append( "<tr>" );
        invoiceEntry.append( "<td>" + startDate + " - " + endDate + "</td>" );
        invoiceEntry.append( "<td>" + distributionInvoiceDTO.getFilmName() + "</td>" );
        invoiceEntry.append( "<td>" + NumbersUtil.formatCurrency( distributionInvoiceDTO.getPricePerWeek() ) + "</td>" );
        invoiceEntry.append( "<td>" + NumbersUtil.formatCurrency( distributionInvoiceDTO.getPriceTotal() ) + "</td>" );
        invoiceEntry.append( "</tr>" );

        return invoiceEntry.toString();
    }

    @Override
    protected String getSavePath()
    {
        return super.getSavePath() + "invoices" + File.separatorChar;
    }

}
