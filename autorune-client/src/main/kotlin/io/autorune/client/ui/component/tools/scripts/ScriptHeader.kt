package io.autorune.client.ui.component.tools.scripts

import io.autorune.client.ui.frame.AutoRuneFrame
import io.autorune.client.ui.component.ARComponent
import io.autorune.client.ui.skin.ColorScheme
import io.autorune.client.ui.utilities.ImageUtil
import io.autorune.script.Script
import io.autorune.script.annotation.ScriptManifest
import java.awt.*
import javax.swing.*

class ScriptHeader(val script: Script?) : JPanel(), ARComponent
{

	private val container = JPanel()

	private val scriptIcon = JLabel()

	private val scriptName = JLabel()

	init
	{

		background = ColorScheme.DARKER_GRAY_COLOR

		container.background = ColorScheme.DARKER_GRAY_COLOR

	}

	override fun setup()
	{

		scriptIcon.icon = ImageIcon(ImageUtil.getResourceStreamFromClass(AutoRuneFrame::class.java, "combat.png"))

		scriptIcon.horizontalAlignment = SwingConstants.CENTER
		scriptIcon.verticalAlignment = SwingConstants.CENTER
		scriptIcon.preferredSize = Dimension(30, 30)

		val manifest = script?.javaClass?.getAnnotation(ScriptManifest::class.java)

		if(manifest != null)
		{

			scriptName.text = manifest.name

		}
		else scriptName.text = "ScriptManifest not found."

		val scriptNameFont = scriptName.font

		scriptName.font = scriptName.font.deriveFont(Font.BOLD, scriptNameFont.size.toFloat())

		container.add(scriptIcon)

		container.add(scriptName)

		add(container)

	}

}