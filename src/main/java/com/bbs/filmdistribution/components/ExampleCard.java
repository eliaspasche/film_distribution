package com.bbs.filmdistribution.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;

/**
 * Example lit template.
 * Base: frontend/src/litelements/examplecard.js
 */
@Tag( "example-card" )
@JsModule( "./src/litelements/examplecard.js" )
public class ExampleCard extends LitTemplate
{

    @Id( "title" )
    private H4 title;

    /**
     * The constructor.
     *
     * @param title The title
     * @param description The description
     */
    public ExampleCard( String title, String description )
    {
        this.title.setText( title );
        getElement().setProperty("desc", description);
    }

}