package io.autorune.client.ui.component.tools.scripts

import io.autorune.client.ui.component.tools.DynamicGridLayout
import io.autorune.client.ui.skin.ColorScheme
import java.awt.BorderLayout
import javax.swing.*
import javax.swing.border.EmptyBorder

class ScriptInfoBox(private val panel : JPanel) : JPanel()
{

	private val container = JPanel()

	private val scriptWrapper = JPanel()

	private val scriptHeader = JPanel(BorderLayout())

	private val scriptInfoPanel = JPanel()

	private val scriptInfoLabels = mutableListOf<JLabel>()

	init
	{

		layout = BorderLayout()
		border = EmptyBorder(5, 0, 0, 0)

		container.layout = BorderLayout()
		container.background = ColorScheme.DARKER_GRAY_COLOR

		scriptWrapper.background = ColorScheme.DARKER_GRAY_COLOR
		scriptWrapper.layout = BorderLayout()
		scriptWrapper.border = EmptyBorder(0, 5, 0, 0)

		val header = ScriptHeader(null)

		header.setup()

		scriptWrapper.add(header, BorderLayout.NORTH)

		scriptInfoPanel.layout = DynamicGridLayout(2, 2)
		scriptInfoPanel.background = ColorScheme.DARK_GRAY_COLOR
		scriptInfoPanel.border = EmptyBorder(6, 5, 0, 2)


		//scriptInfoPanel.add(scriptName)

		add(container, BorderLayout.NORTH)

	}

	fun update()
	{

		SwingUtilities.invokeLater {

			if(parent !== panel)
			{
				panel.add(this)

				container.removeAll()

				container.add(scriptWrapper, BorderLayout.WEST)
				container.add(scriptInfoPanel, BorderLayout.CENTER)

				panel.revalidate()
			}

		}

	}

}