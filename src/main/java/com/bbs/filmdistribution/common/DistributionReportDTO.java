package com.bbs.filmdistribution.common;

/**
 * Interface for assigning values from a sql query for reporting.
 */
public interface DistributionReportDTO extends DistributionInvoiceDTO
{
    Long getCustomerId();

    Integer getAmount();
}
