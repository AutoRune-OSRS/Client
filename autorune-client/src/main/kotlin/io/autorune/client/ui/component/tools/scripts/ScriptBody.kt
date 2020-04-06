package io.autorune.client.ui.component.tools.scripts

import javax.swing.JLabel
import javax.swing.JPanel

class ScriptBody : JPanel()
{

	val infoPanel = JPanel()

	init
	{

		infoPanel.add(JLabel("Runtime: 192 days"))

		add(infoPanel)

	}

}