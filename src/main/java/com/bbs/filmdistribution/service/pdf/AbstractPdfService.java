package com.bbs.filmdistribution.service.pdf;

import com.bbs.filmdistribution.config.AppConfig;
import com.bbs.filmdistribution.service.FileDownloadService;
import com.bbs.filmdistribution.util.NotificationUtil;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.io.ClassPathResource;
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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base implementation to create pdf files.
 */
@RequiredArgsConstructor
public abstract class AbstractPdfService {

    public static final String PATH_TO_PDF_TEMPLATES = "META-INF" + File.separatorChar + "resources" + File.separatorChar + "pdf-templates" + File.separatorChar;

    private static final Logger LOGGER = Logger.getLogger(AbstractPdfService.class.getName());

    // Services
    private final AppConfig appConfig;
    private final FileDownloadService fileDownloadService;

    /**
     * Get the save path of a created pdf file.
     *
     * @return The path as string.
     */
    protected String getSavePath() {
        return appConfig.getPdfSavePath() + File.separatorChar;
    }

    /**
     * Load an CSS style by from the resources for a pdf file.
     *
     * @param styleName The name of the CSS file
     * @return The url to the file
     */
    protected Optional<String> loadPdfStyle( String styleName )
    {
        ClassPathResource resource = new ClassPathResource( PATH_TO_PDF_TEMPLATES + styleName );

        try {
            URL fileUrl = resource.getURL();
            Path path = Paths.get(fileUrl.toURI());
            return Optional.of(path.toUri().toURL().toString());
        } catch (IOException | URISyntaxException e) {
            LOGGER.log(Level.WARNING, "Pdf style cannot loaded", e);
        }

        return Optional.empty();
    }

    /**
     * Load html structure from a template to build a pdf file.
     *
     * @param htmlTemplateName The name of the template (example: template.html)
     * @return The input as string
     */
    protected Optional<String> loadPdfTemplate(String htmlTemplateName) {

        ClassPathResource resource = new ClassPathResource(PATH_TO_PDF_TEMPLATES + htmlTemplateName);
        try {
            URL fileUrl = resource.getURL();
            Path path = Paths.get(fileUrl.toURI());
            return Optional.of(String.join("\n", Files.readAllLines(path, StandardCharsets.UTF_8)));
        } catch (IOException | URISyntaxException e) {
            LOGGER.log(Level.WARNING, "Pdf template cannot loaded", e);
        }

        return Optional.empty();
    }

    /**
     * Create {@link Document} from html text for pdf file.
     *
     * @param htmlText The html text.
     * @return The {@link Document}
     */
    protected Document createDocumentFromHtmlText(String htmlText) {
        Document document = Jsoup.parse(htmlText, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        return document;
    }

    /**
     * Get a {@link Element} by an {@link Document} and the defined id.
     *
     * @param htmlDocument The {@link Document}
     * @param id           The id of element in the {@link Document}
     * @return The {@link Element}
     */
    protected Element getElementByDocument(Document htmlDocument, String id) {
        return htmlDocument.getElementById(id);
    }

    /**
     * Create the directory path to store the created pdf files.
     */
    private void createPdfSavePath() {
        Path pathToCreate = Path.of(getSavePath());
        if (Files.exists(pathToCreate)) {
            return;
        }

        try {
            Files.createDirectories(pathToCreate);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            NotificationUtil.sendErrorNotification("The pdf file path cannot be created. Cause: " + e.getCause(), 5);
        }
    }

    /**
     * Create pdf file by {@link Document} with specific filename and style.
     *
     * @param htmlDocument The {@link Document}
     * @param pdfFileName  The name of pdf file
     */
    protected void createPdfFile( Document htmlDocument, String pdfFileName, String styleUrl )
    {
        createPdfSavePath();
        File outputPdf = new File(getSavePath() + pdfFileName + ".pdf");

        try (OutputStream outputStream = new FileOutputStream(outputPdf)) {
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

            renderer.createPDF(outputStream);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Pdf can not be created", e);
        } finally {
            fileDownloadService.downloadFileFromStreamLink(outputPdf.getAbsolutePath());
        }
    }
}
