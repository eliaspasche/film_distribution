package com.bbs.filmdistribution.util;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

/**
 * Utility class to create components for the application
 */
public class ComponentUtil
{

    /**
     * Create {@link NumberField} with label and suffix.
     *
     * @param labelText  The label
     * @param suffixText The suffix
     * @return Created {@link NumberField}
     */
    public static NumberField createNumberField( String labelText, String suffixText )
    {
        NumberField numberField = new NumberField( labelText );
        Div euroSuffix = new Div();
        euroSuffix.setText( suffixText );
        numberField.setSuffixComponent( euroSuffix );

        return numberField;
    }

    /**
     * Create {@link IntegerField} with label and suffix.
     *
     * @param labelText  The label
     * @param suffixText The suffix
     * @return Created {@link IntegerField}
     */
    public static IntegerField createIntegerField( String labelText, String suffixText )
    {
        IntegerField integerField = new IntegerField( labelText );
        Div euroSuffix = new Div();
        euroSuffix.setText( suffixText );
        integerField.setSuffixComponent( euroSuffix );

        return integerField;
    }

    /**
     * Create a {@link TextField} for searching in a grid.
     *
     * @param labelText The text
     * @return The {@link TextField}
     */
    public static TextField createGridSearchField( String labelText )
    {
        TextField textField = new TextField( labelText );
        textField.setPrefixComponent( VaadinIcon.SEARCH.create() );
        textField.addThemeVariants( TextFieldVariant.LUMO_SMALL );
        textField.setClearButtonVisible( true );

        return textField;
    }

}
