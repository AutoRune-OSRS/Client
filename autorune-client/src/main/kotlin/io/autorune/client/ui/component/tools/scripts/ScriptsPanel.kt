package io.autorune.client.ui.component.tools.scripts

import io.autorune.client.ui.component.tools.ClientToolPanel
import javax.swing.*

class ScriptsPanel : ClientToolPanel()
{

	val scriptOptions = ScriptOptions()

	val header = ScriptHeader(null)

	val body = ScriptBody()

	init
	{

		layout = BoxLayout(this, BoxLayout.Y_AXIS)

		header.setup()

		add(scriptOptions)

		add(header)

		add(body)

	}

}