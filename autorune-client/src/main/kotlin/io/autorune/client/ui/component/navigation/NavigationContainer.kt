package io.autorune.client.ui.component.navigation

import com.google.common.eventbus.Subscribe
import io.autorune.client.AutoRune
import io.autorune.client.event.navigation.NavigationButtonAdded
import io.autorune.client.event.navigation.NavigationButtonRemoved
import io.autorune.client.event.sidebar.*
import io.autorune.client.ui.frame.container.RootContentContainer
import io.autorune.client.ui.component.tools.ClientToolPanel
import io.autorune.client.ui.utilities.SwingUtil
import org.pushingpixels.substance.internal.SubstanceSynapse
import java.awt.*
import java.util.function.BiConsumer
import javax.swing.*

class NavigationContainer(val container : RootContentContainer) : JPanel(BorderLayout())
{

	internal var currentPanel : ClientToolPanel? = null

	init
	{

		minimumSize = Dimension(0, 0)
		maximumSize = Dimension(0, 0)
		preferredSize = Dimension(0, 0)

		putClientProperty(SubstanceSynapse.COLORIZATION_FACTOR, 1.0)

		AutoRune.eventBus.register(this)

	}

	@Subscribe fun onNavigationButtonAdded(event : NavigationButtonAdded)
	{

		SwingUtilities.invokeLater {

			val navigationButton : NavigationButton = event.button

			val pluginPanel = navigationButton.panel

			val inTitle = !event.button.tab

			val iconSize = 16

			val button = SwingUtil.createSwingButton(navigationButton, iconSize, BiConsumer { navButton, jButton ->

				val panel = navButton.panel ?: return@BiConsumer

				val doClose = panel.isActive

				if(doClose)
					AutoRune.eventBus.post(RemoveSidebarPanelEvent(pluginPanel!!))
				else
					AutoRune.eventBus.post(AddSidebarPanelEvent(pluginPanel!!))

			})

			if(inTitle)
			{
				container.titleToolbar.addComponent(event.button, button)
				container.titleToolbar.revalidate()
			}
			else
			{
				container.pluginToolbar.addComponent(event.button, button)
				container.pluginToolbar.revalidate()
			}

			navigationButton.onReady()

		}

	}

	@Subscribe fun onNavigationButtonRemoved(event : NavigationButtonRemoved)
	{

		SwingUtilities.invokeLater {

			container.pluginToolbar.removeComponent(event.button)
			container.pluginToolbar.revalidate()
			container.titleToolbar.removeComponent(event.button)
			container.titleToolbar.revalidate()

			val pluginPanel = event.button.panel

			remove(pluginPanel?.wrappedPanel)

		}

	}

}