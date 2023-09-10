package com.bbs.filmdistribution.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import lombok.Getter;

/**
 * Layout for the home view.
 * Base: frontend/src/litelements/homelayout.ts
 */
@Tag( "home-layout" )
@JsModule( "./src/litelements/homelayout.ts" )
@Getter
public class HomeLayout extends LitTemplate
{

    @Id( "info-layout" )
    private Div infoLayout;

    @Id( "layout" )
    private Div layout;

}