package com.bbs.filmdistribution.common;

import java.time.LocalDate;

/**
 * Interface to map values from the database in a query.
 */
public interface DistributionInvoiceDTO
{

    String getFilmName();

    LocalDate getStartDate();

    LocalDate getEndDate();

    Double getPricePerWeek();

    Double getPriceTotal();

}
