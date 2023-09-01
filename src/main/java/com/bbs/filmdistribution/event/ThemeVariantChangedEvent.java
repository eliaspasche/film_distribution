package com.bbs.filmdistribution.event;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

/**
 * Event to handle theme change (dark/light)
 */
@Getter
public class ThemeVariantChangedEvent extends ComponentEvent<UI>
{

	private final String themeName;

	/**
	 * The constructor.
	 *
	 * @param source The {@link UI}
	 * @param themeName The name of the changed theme
	 */
	public ThemeVariantChangedEvent( UI source, String themeName )
	{
		super( source, false );
		this.themeName = themeName;
	}

	/**
	 * Register the event.
	 *
	 * @param ui The {@link UI}
	 * @param listener The {@link ComponentEventListener}
	 * @return The {@link Registration}
	 */
	public static Registration addThemeChangedListener( UI ui,
			ComponentEventListener<ThemeVariantChangedEvent> listener )
	{
		return ComponentUtil.addListener( ui, ThemeVariantChangedEvent.class, listener );
	}

}
