package com.bbs.filmdistribution.common;

import com.bbs.filmdistribution.data.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Bean for storing report data. Used for pdf export only.
 */
@Getter
@Setter
public class DistributionReport {

    Customer customer;
    List<DistributionReportItem> items;
    Double totalNet;


    /**
     * Bean for single items of a report
     */
    @Getter
    @Setter
    public static class DistributionReportItem {
        LocalDate startDate;
        LocalDate endDate;
        String film;
        Double pricePerWeek;
        Double total;
    }
}
