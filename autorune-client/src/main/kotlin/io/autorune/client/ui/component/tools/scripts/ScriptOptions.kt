package io.autorune.client.ui.component.tools.scripts

import javax.swing.JButton
import javax.swing.JPanel

class ScriptOptions : JPanel()
{

	private val refreshLocalScripts = JButton("Refresh")

	init
	{

		add(refreshLocalScripts)

	}

}