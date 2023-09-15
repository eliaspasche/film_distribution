package com.bbs.filmdistribution.service.pdf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service to create pdf files.
 */
@Component
public class PdfService
{

    private static final Logger LOGGER = Logger.getLogger( PdfService.class.getName() );

    public static final String PATH_TO_PDF_TEMPLATES = "META-INF" + File.separatorChar + "resources" + File.separatorChar + "pdf-templates" + File.separatorChar;

    /**
     * Load an CSS style by from the resources for a pdf file.
     *
     * @param styleName The name of the CSS file
     * @return The url to the file
     */
    private Optional<String> loadPdfStyle( String styleName )
    {
        ClassPathResource resource = new ClassPathResource( PATH_TO_PDF_TEMPLATES + styleName );

        try
        {
            URL fileUrl = resource.getURL();
            Path path = Paths.get( fileUrl.toURI() );
            return Optional.of( path.toUri().toURL().toString() );
        }
        catch ( IOException | URISyntaxException e )
        {
            LOGGER.log( Level.WARNING, "Pdf style cannot loaded", e );
        }

        return Optional.empty();
    }

    /**
     * Load html structure from a template to build a pdf file.
     *
     * @param templateName The name of the template
     * @return The input as string
     */
    private Optional<String> loadPdfTemplate( String templateName )
    {

        ClassPathResource resource = new ClassPathResource( PATH_TO_PDF_TEMPLATES + templateName );
        try
        {
            URL fileUrl = resource.getURL();
            Path path = Paths.get( fileUrl.toURI() );
            return Optional.of( String.join( "\n", Files.readAllLines( path, StandardCharsets.UTF_8 ) ) );
        }
        catch ( IOException | URISyntaxException e )
        {
            LOGGER.log( Level.WARNING, "Pdf template cannot loaded", e );
        }

        return Optional.empty();
    }

    /**
     * Create {@link Document} from html text for pdf file.
     *
     * @param htmlText The html text.
     * @return The {@link Document}
     */
    private Document createDocumentFromHtmlText( String htmlText )
    {
        Document document = Jsoup.parse( htmlText, "UTF-8" );
        document.outputSettings().syntax( Document.OutputSettings.Syntax.xml );

        return document;
    }


    public void createInvoicePdf()
    {
        String fileInput = loadPdfTemplate( "film-invoice.html" ).get();

        Document htmlDocument = createDocumentFromHtmlText( fileInput );
        // Fill customer data
        htmlDocument.getElementById( "customerNumber" ).appendText( "000001" );
        htmlDocument.getElementById( "customerName" ).appendText( "John Doe" );

        Element filmTable = htmlDocument.getElementById( "table" );
        for ( int i = 0; i < 23; i++ )
        {
            filmTable.append( buildInvoiceEntry() );
        }

        createPdfFile( htmlDocument, "test", "film-invoice.css" );
    }

    private String buildInvoiceEntry()
    {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder invoiceEntry = new StringBuilder();
        invoiceEntry.append( "<tr>" );
        invoiceEntry.append( "<td>01.01.2023 - 20.01.2023</td>" );
        invoiceEntry.append( "<td>" + secureRandom.nextInt( 100 ) + " €</td>" );
        invoiceEntry.append( "<td>22</td>" );
        invoiceEntry.append( "</tr>" );

        return invoiceEntry.toString();
    }


    /**
     * Create pdf file by {@link Document} with specific filename and style.
     *
     * @param htmlDocument The {@link Document}
     * @param pdfFileName  The name of pdf file
     * @param styleUrl     The style url
     */
    private void createPdfFile( Document htmlDocument, String pdfFileName, String styleUrl )
    {

        File outputPdf = new File( "./" + pdfFileName + ".pdf" );
        try ( OutputStream outputStream = new FileOutputStream( outputPdf ) )
        {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint( true );
            sharedContext.setInteractive( false );
            sharedContext.setReplacedElementFactory( new ImageElementFactoryImpl() );
            if ( styleUrl != null )
            {
                loadPdfStyle( styleUrl ).ifPresent( style -> renderer.setDocumentFromString( htmlDocument.html(), style ) );
            }
            else
            {
                renderer.setDocumentFromString( htmlDocument.html() );
            }

            renderer.layout();
            renderer.createPDF( outputStream );
        }
        catch ( IOException e )
        {
            LOGGER.log( Level.WARNING, "Pdf can not be created", e );
        }
    }

    /**
     * Create pdf file by {@link Document} with specific filename.
     *
     * @param htmlDocument The {@link Document}
     * @param pdfFileName  The name of pdf file
     */
    private void createPdfFile( Document htmlDocument, String pdfFileName )
    {
        createPdfFile( htmlDocument, pdfFileName, null );
    }

}
