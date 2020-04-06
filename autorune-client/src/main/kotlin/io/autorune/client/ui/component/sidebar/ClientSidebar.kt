package io.autorune.client.ui.component.sidebar

import com.google.common.collect.ComparisonChain
import com.google.common.eventbus.Subscribe
import io.autorune.client.AutoRune
import io.autorune.client.event.sidebar.*
import io.autorune.client.ui.component.navigation.NavigationButton
import io.autorune.client.ui.frame.AutoRuneFrame
import io.autorune.client.ui.frame.container.RootContentContainer
import io.autorune.client.ui.component.tools.ClientToolbar
import io.autorune.client.ui.component.tools.scripts.ScriptsPanel
import io.autorune.client.ui.skin.ColorScheme
import io.autorune.client.ui.utilities.ImageUtil
import java.awt.*
import java.util.*
import javax.swing.Box
import javax.swing.JPanel
import kotlin.Comparator

class ClientSidebar(val container : RootContentContainer) : JPanel()
{

	private val toolbarWidth = 36

	private val toolbarHeight = 503

	private val componentMap : MutableMap<NavigationButton, Component?> = TreeMap(Comparator { a : NavigationButton, b : NavigationButton ->
		ComparisonChain
				.start()
				.compareTrueFirst(a.tab, b.tab)
				.compare(a.priority, b.priority)
				.compare(a.toolTip, b.toolTip)
				.result()
	})

	internal var isExpanded = false

	init
	{

		background = ColorScheme.DARKER_GRAY_COLOR

		AutoRune.eventBus.register(this)

		size = Dimension(toolbarWidth, toolbarHeight)
		minimumSize = Dimension(toolbarWidth, toolbarHeight)
		preferredSize = Dimension(toolbarWidth, toolbarHeight)
		maximumSize = Dimension(toolbarWidth, Int.MAX_VALUE)

		val settingsIcon = ImageUtil.getResourceStreamFromClass(AutoRuneFrame::class.java, "settings.png")

		val settingsRolloverIcon = ImageUtil.getResourceStreamFromClass(AutoRuneFrame::class.java, "settings_hover.png")

		val settingsButton = NavigationButton(settingsIcon, settingsRolloverIcon, true, "", { }, false, { }, { }, 3, null, ScriptsPanel())

		ClientToolbar.addNavigation(settingsButton)


	}

	fun addComponent(button : NavigationButton, c : Component?)
	{
		if(componentMap.put(button, c) == null)
		{
			update()
		}
	}

	fun removeComponent(button : NavigationButton?)
	{
		if(componentMap.remove(button) != null)
		{
			update()
		}
	}

	private fun update()
	{
		removeAll()
		var isDelimited = false
		for((key, value) in componentMap)
		{
			if(!key.tab && !isDelimited)
			{
				isDelimited = true
				add(Box.createVerticalGlue())
			}
			add(value)
		}
		repaint()
	}

	@Subscribe fun onRemoveSidebarPanel(event : RemoveSidebarPanelEvent)
	{

		val panel = event.panel

		panel.onDeactivate()

		val width : Int = panel.wrappedPanel.preferredSize.width

		val contractBy = if(panel.isActive) panel.wrappedPanel.preferredSize.width - width else width

		container.navContainer.minimumSize = Dimension(toolbarWidth, toolbarHeight)
		container.navContainer.maximumSize = Dimension(toolbarWidth, Integer.MAX_VALUE)
		container.navContainer.preferredSize = Dimension(toolbarWidth, toolbarHeight)

		container.navContainer.currentPanel = null

		container.navContainer.remove(panel)

		container.navContainer.revalidate()

		container.frame.contractBy(contractBy)

		container.revalidate()

	}

	@Subscribe fun onAddSidebarPanel(event: AddSidebarPanelEvent)
	{

		val panel = event.panel

		panel.onActivate()

		val expandBy : Int = panel.wrappedPanel.preferredSize.width

		container.navContainer.minimumSize = Dimension(expandBy + toolbarWidth, toolbarHeight)
		container.navContainer.maximumSize = Dimension(expandBy + toolbarWidth, Integer.MAX_VALUE)
		container.navContainer.preferredSize = Dimension(expandBy + toolbarWidth, toolbarHeight)

		container.navContainer.currentPanel = panel

		container.navContainer.add(panel, BorderLayout.CENTER)

		container.navContainer.revalidate()

		container.frame.expandBy(expandBy)

		container.revalidate()

	}

	@Subscribe fun onExpandSidebar(event : ExpandSidebarEvent)
	{

		val toolPanel = container.navContainer.currentPanel

		toolPanel?.onActivate()

		if(!isExpanded)
			AutoRune.eventBus.post(ToggleSidebarEvent(toolPanel))

		var width = toolbarWidth

		if(toolPanel != null && toolPanel.isActive)
			width += toolPanel.wrappedPanel.preferredSize.width

		// Expand sidebar
		container.navContainer.minimumSize = Dimension(width, 0)
		container.navContainer.maximumSize = Dimension(width, Int.MAX_VALUE)
		container.navContainer.preferredSize = Dimension(width, 0)

		container.navContainer.add(container.pluginToolbar, BorderLayout.EAST)

		container.navContainer.revalidate()

		if(width > 0)
			container.frame.expandBy(width)
		else if(width < 0)
			container.frame.contractBy(width)

		container.updateSideBarIcon()

		container.revalidate()

	}

	@Subscribe fun onContractSidebar(event : ContractSidebarEvent)
	{

		val toolPanel = container.navContainer.currentPanel

		toolPanel?.onDeactivate()

		container.navContainer.minimumSize = Dimension(0, 0)
		container.navContainer.maximumSize = Dimension(0, 0)
		container.navContainer.preferredSize = Dimension(0, 0)

		var width = toolbarWidth

		if(toolPanel != null)
			width += toolPanel.wrappedPanel.preferredSize.width

		container.frame.contractBy(width)

		container.navContainer.remove(container.pluginToolbar)

		container.navContainer.revalidate()

		container.updateSideBarIcon()

		container.revalidate()

	}

	@Subscribe fun onToggleSidebar(event : ToggleSidebarEvent)
	{

		val isSidebarOpen = isExpanded

		isExpanded = !isExpanded

		if(isSidebarOpen)
			AutoRune.eventBus.post(ContractSidebarEvent(event.toolPanel))
		else
			AutoRune.eventBus.post(ExpandSidebarEvent(event.toolPanel))

		container.navContainer.revalidate()

		container.revalidate()

	}

}