package com.bbs.filmdistribution.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class to handle date values.
 */
// Private constructor to hide the implicit public one
@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class DateUtil
{

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern( DATE_FORMAT );

    /**
     * Format a {@link LocalDate}
     *
     * @param dateToFormat The {@link LocalDate}
     * @return The formatted date as string
     */
    public static String formatDate( LocalDate dateToFormat )
    {
        return dateToFormat.format( DATE_TIME_FORMATTER );
    }

    /**
     * Get the age of a specific {@link LocalDate}
     *
     * @param date The {@link LocalDate} to check
     * @return The age
     */
    public static int getAgeByDate( LocalDate date )
    {
        return Period.between( date, LocalDate.now() ).getYears();
    }

    /**
     * Returns the current date.
     *
     * @return LocalDate
     */
    public static LocalDate now() {
        return LocalDate.now(ZoneId.systemDefault());
    }

}
