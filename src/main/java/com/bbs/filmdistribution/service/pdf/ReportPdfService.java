package com.bbs.filmdistribution.service.pdf;

import com.bbs.filmdistribution.common.DistributionInvoiceDTO;
import com.bbs.filmdistribution.common.DistributionReport;
import com.bbs.filmdistribution.common.DistributionReportDTO;
import com.bbs.filmdistribution.config.AppConfig;
import com.bbs.filmdistribution.data.entity.Customer;
import com.bbs.filmdistribution.data.entity.Film;
import com.bbs.filmdistribution.data.service.CustomerService;
import com.bbs.filmdistribution.data.service.FilmDistributionService;
import com.bbs.filmdistribution.service.FileDownloadService;
import com.bbs.filmdistribution.util.DateUtil;
import com.bbs.filmdistribution.util.NumbersUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Service to create pdf file for distribution report.
 */
@Component
public class ReportPdfService extends AbstractPdfService {

    private final FilmDistributionService filmDistributionService;
    private final CustomerService customerService;

    /**
     * The constructor.
     *
     * @param appConfig               The {@link AppConfig}
     * @param fileDownloadService     The {@link FileDownloadService}
     * @param filmDistributionService The {@link FilmDistributionService}
     */
    public ReportPdfService(AppConfig appConfig, FileDownloadService fileDownloadService, FilmDistributionService filmDistributionService, CustomerService customerService) {
        super(appConfig, fileDownloadService);
        this.filmDistributionService = filmDistributionService;
        this.customerService = customerService;
    }


    public void createReportPdf(Customer customer, Film film, LocalDate reportingDate) {
        List<DistributionReportDTO> rawReport = filmDistributionService.getDistributionReportForSelectedFilter(customer != null ? customer.getId() : null, film != null ? film.getId() : null, reportingDate);

        Map<Long, List<DistributionReportDTO>> rawReportGrouped = rawReport.stream().collect(Collectors.groupingBy(DistributionReportDTO::getCustomerId));

        AtomicReference<Double> totalNet = new AtomicReference<>(0d);
        List<DistributionReport> reports = rawReportGrouped.entrySet().stream().map(entry -> mapToReport(entry, totalNet)).toList();

        String headerInput = loadPdfTemplate("header.html").orElse("");
        String fileInput = loadPdfTemplate("report.html").orElse("");

        Document htmlDocument = createDocumentFromHtmlText(headerInput + fileInput);

        String now = DateUtil.formatDate(LocalDate.now());
        String formattedReportDate = reportingDate != null ? DateUtil.formatDate(reportingDate) : now;

        // Fill meta data
        getElementByDocument(htmlDocument, "reportDate").appendText(formattedReportDate);
        getElementByDocument(htmlDocument, "issueDate").appendText(now);

        // Fill film costs data
        Element reportTable = getElementByDocument(htmlDocument, "reportTable");

        reports.forEach(report -> reportTable.append(buildReportTableEntry(report)));

        double tax = totalNet.get() * 0.19;
        getElementByDocument(htmlDocument, "totalNet").appendText(NumbersUtil.formatCurrency(totalNet.get()));
        getElementByDocument(htmlDocument, "totalTax").appendText(NumbersUtil.formatCurrency(tax));
        getElementByDocument(htmlDocument, "total").appendText(NumbersUtil.formatCurrency(totalNet.get() + tax));

        createPdfFile(htmlDocument, "Report_" + now, "pdf-template.css");
    }

    private DistributionReport mapToReport(Map.Entry<Long, List<DistributionReportDTO>> entry, AtomicReference<Double> totalNet) {
        DistributionReport report = new DistributionReport();

        customerService.get(entry.getKey()).ifPresent(report::setCustomer);

        AtomicReference<Double> totalNetReportItem = new AtomicReference<>(0d);
        List<DistributionReport.DistributionReportItem> reportItems = entry.getValue().stream().map(item -> mapToReportItem(item, totalNetReportItem)).toList();

        report.setItems(reportItems);
        report.setTotalNet(totalNetReportItem.get());

        totalNet.updateAndGet(v -> (v + totalNetReportItem.get()));
        return report;
    }

    private static DistributionReport.DistributionReportItem mapToReportItem(DistributionReportDTO item, AtomicReference<Double> totalNetInside) {
        DistributionReport.DistributionReportItem reportItem = new DistributionReport.DistributionReportItem();
        reportItem.setStartDate(item.getStartDate());
        reportItem.setEndDate(item.getEndDate());
        reportItem.setFilm(item.getFilmName());
        reportItem.setPricePerWeek(item.getPricePerWeek());
        reportItem.setTotal(item.getPriceTotal());

        totalNetInside.updateAndGet(v -> (v + item.getPriceTotal()));
        return reportItem;
    }

    /**
     * Build invoice table entry in the pdf.
     *
     * @param reportItem The {@link DistributionInvoiceDTO}
     * @return Created html text that will be inserted into the table.
     */
    private String buildReportItem(DistributionReport.DistributionReportItem reportItem) {
        String startDate = DateUtil.formatDate(reportItem.getStartDate());
        String endDate = DateUtil.formatDate(reportItem.getEndDate());

        return "<tr>" +
                "<td><p>" + startDate + " - " + endDate + "</p></td>" +
                "<td><p>" + reportItem.getFilm() + "</p></td>" +
                "<td><p>" + NumbersUtil.formatCurrency(reportItem.getPricePerWeek()) + "</p></td>" +
                "<td><p>" + NumbersUtil.formatCurrency(reportItem.getTotal()) + "</p></td>" +
                "</tr>";
    }

    private String buildReportTableEntry(DistributionReport report) {
        StringBuilder tableEntry = new StringBuilder();
        tableEntry.append("<tr>");
        tableEntry.append("<div class=\"reportItem\">");
        tableEntry.append("<div>");

        tableEntry.append("<h5>Kundennummer: ").append(NumbersUtil.createLeadingZeroCustomerNumber(report.getCustomer().getId())).append("</h5>");
        tableEntry.append("<p>").append(report.getCustomer().getFullName()).append("<br>");
        tableEntry.append("<p>").append(report.getCustomer().getAddress()).append("<br>");
        tableEntry.append("<p>").append(report.getCustomer().getZipCode()).append(" ").append(report.getCustomer().getCity()).append("</p>");

        tableEntry.append("<table class=\"invoiceTable\">");
        tableEntry.append("<tbody>");
        tableEntry.append("<tr>");
        tableEntry.append("<th><p>Zeitraum</p></th>");
        tableEntry.append("<th><p>Film</p></th>");
        tableEntry.append("<th><p>Preis/Woche</p></th>");
        tableEntry.append("<th><p>Kosten</p></th>");
        tableEntry.append("</tr>");

        report.getItems().forEach(item -> tableEntry.append(buildReportItem(item)));

        tableEntry.append("</tbody>");
        tableEntry.append("</table>");

        tableEntry.append("<div class=\"costs\">");
        tableEntry.append("<table class=\"costsTable\">");
        tableEntry.append("<tr>");
        tableEntry.append("<th><p>Summe (Netto):</p></th>");
        tableEntry.append("<th><p>").append(NumbersUtil.formatCurrency(report.getTotalNet())).append("</p></th>");
        tableEntry.append("</tr>");
        tableEntry.append("</table>");
        tableEntry.append("</div>");


        tableEntry.append("</div>");
        tableEntry.append("</div>");
        tableEntry.append("</tr>");

        return tableEntry.toString();
    }

    @Override
    protected String getSavePath() {
        return super.getSavePath() + "invoices" + File.separatorChar;
    }

}
