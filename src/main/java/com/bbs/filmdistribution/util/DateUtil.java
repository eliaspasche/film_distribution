package com.bbs.filmdistribution.util;

import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.function.ValueProvider;

import java.time.LocalDate;

/**
 * Utility class to handle date values.
 */
public class DateUtil
{

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    /**
     * The constructor
     */
    private DateUtil()
    {
        // Private constructor to hide the implicit public one
    }

    /**
     * Create a {@link LocalDateRenderer} to display a formatted {@link LocalDate}
     *
     * @param valueProvider The {@link ValueProvider}
     * @return Created {@link LocalDateRenderer}
     * @param <T> The object type
     */
    public static <T> LocalDateRenderer<T> createLocalDateRenderer( ValueProvider<T, LocalDate> valueProvider )
    {
        return new LocalDateRenderer<>( valueProvider, DATE_FORMAT );
    }

}
