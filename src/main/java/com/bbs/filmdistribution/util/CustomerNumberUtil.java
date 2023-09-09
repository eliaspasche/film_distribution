package com.bbs.filmdistribution.util;

/**
 * Utility class to create a customer number
 */
public class CustomerNumberUtil
{

    /**
     * The constructor
     */
    private CustomerNumberUtil()
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

}
