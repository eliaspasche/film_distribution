package com.bbs.filmdistribution.common;

import com.bbs.filmdistribution.data.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DistributionReport {

    Customer customer;
    List<DistributionReportItem> items;
    Double totalNet;


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
