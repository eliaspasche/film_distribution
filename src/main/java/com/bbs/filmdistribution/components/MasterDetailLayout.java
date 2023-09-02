package com.bbs.filmdistribution.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import lombok.Getter;

/**
 * Master detail layout.
 * Include a {@link SplitLayout} with a primary and secondary (editor) area.
 * CSS styles in (frontend/themes/filmdistribution/components/master-detail-layout.css)
 */
@Getter
public class MasterDetailLayout extends Div
{

    private final Div primaryDiv;
    private final Div secondaryDiv;
    private final Div editorDiv;

    /**
     * Build the layout.
     */
    public MasterDetailLayout()
    {
        addClassNames( "master-detail" );
        SplitLayout splitLayout = new SplitLayout();

        add( splitLayout );

        this.primaryDiv = new Div();
        primaryDiv.setClassName( "grid-wrapper" );
        splitLayout.addToPrimary( primaryDiv );

        this.secondaryDiv = new Div();
        secondaryDiv.setClassName( "editor-layout" );

        this.editorDiv = new Div();
        editorDiv.setClassName( "editor" );
        secondaryDiv.add( editorDiv );

        splitLayout.addToSecondary( secondaryDiv );
    }

}
