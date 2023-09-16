package com.bbs.filmdistribution.util;

/**
 * Utility class to create and format numbers
 */
public class NumbersUtil
{

    /**
     * The constructor
     */
    private NumbersUtil()
    {
        // Private constructor to hide the implicit public one
    }

    /**
     * Create a customer number with 7 leading zeros. ( 1 -> 0000001 )
     *
     * @param number The number to format.
     * @return The formatted number
     */
    public static String createLeadingZeroCustomerNumber( long number )
    {
        return String.format( "%07d", number );
    }

    /**
     * Format a double value to currency text. (10.1 -> 10.10 €)
     *
     * @param value The value to format
     * @return The formatted value.
     */
    public static String formatCurrency( double value )
    {
        return String.format( "%,.2f €", value );
    }
}
