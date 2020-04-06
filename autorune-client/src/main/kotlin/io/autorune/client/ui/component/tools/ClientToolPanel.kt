package io.autorune.client.ui.component.tools

import io.autorune.client.ui.component.ARComponent
import io.autorune.client.ui.skin.ColorScheme
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder

abstract class ClientToolPanel : JPanel(), ARComponent
{

	private var scrollPane : JScrollPane

	var isActive = false

	val wrappedPanel = JPanel()

	private val northPanel = JPanel()

	private val panelWidth = 225

	private val scrollbarWidth = 17

	private val borderOffset = 6

	fun onActivate()
	{
		isActive = true
	}

	fun onDeactivate()
	{
		isActive = false
	}

	init
	{

		border = EmptyBorder(borderOffset, borderOffset, borderOffset, borderOffset)

		layout = DynamicGridLayout(0, 1, 0, 3)

		background = ColorScheme.DARK_GRAY_COLOR

		northPanel.layout = BorderLayout()

		northPanel.background = ColorScheme.DARK_GRAY_COLOR

		scrollPane = JScrollPane(northPanel)

		scrollPane.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER

		// Adjust the preferred size to expand to width of scrollbar to
		// to prevent scrollbar overlapping over contents

		wrappedPanel.preferredSize = Dimension(panelWidth + scrollbarWidth, 0)

		wrappedPanel.layout = BorderLayout()

	}

	override fun setup()
	{

		northPanel.add(this, BorderLayout.NORTH)

		wrappedPanel.add(scrollPane, BorderLayout.CENTER)

	}

	override fun getPreferredSize() : Dimension
	{
		val width = if(this === wrappedPanel) panelWidth + scrollbarWidth else panelWidth
		return Dimension(width, super.getPreferredSize().height)
	}


}