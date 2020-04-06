package io.autorune.client.ui.component.tools

import io.autorune.client.AutoRune.eventBus
import io.autorune.client.event.navigation.NavigationButtonAdded
import io.autorune.client.event.navigation.NavigationButtonRemoved
import io.autorune.client.ui.component.navigation.NavigationButton

object ClientToolbar
{

	private val buttons = mutableSetOf<NavigationButton>()
	/**
	 * Add navigation.
	 *
	 * @param button the button
	 */
	fun addNavigation(button : NavigationButton)
	{
		if(buttons.contains(button))
		{
			return
		}
		if(buttons.add(button))
		{
			eventBus.post(NavigationButtonAdded(button))
		}
	}

	/**
	 * Remove navigation.
	 *
	 * @param button the button
	 */
	fun removeNavigation(button : NavigationButton)
	{
		if(buttons.remove(button))
		{
			eventBus.post(NavigationButtonRemoved(button))
		}
	}

}