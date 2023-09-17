package com.bbs.filmdistribution.common;

import java.time.LocalDate;

/**
 * Interface for assigning values from a sql query.
 */
public interface DistributionInvoiceDTO
{

    String getFilmName();

    LocalDate getStartDate();

    LocalDate getEndDate();

    Double getPricePerWeek();

    Double getPriceTotal();

}
