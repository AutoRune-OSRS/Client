package io.autorune.client.ui.component.script

import io.autorune.client.AutoRune
import io.autorune.script.Script
import io.autorune.script.executor.ScriptExecutor
import io.autorune.script.annotation.ScriptManifest
import io.autorune.script.loader.ScriptLoader
import java.awt.Dimension
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.event.MouseInputListener

class ScriptSelector(private val scriptExecutor : ScriptExecutor) : JFrame(), MouseInputListener
{

	private val scriptDisplayPanel = JPanel()

	private val scriptToggles = mutableListOf<JRadioButton>()

	private val scriptButtons = mutableMapOf<JRadioButton, Script>()

	private val scriptLoader = ScriptLoader()

	init
	{

		preferredSize = Dimension(400, 300)

		size = preferredSize

		setLocation(AutoRune.AUTORUNE_FRAME.x + AutoRune.AUTORUNE_FRAME.width, AutoRune.AUTORUNE_FRAME.y);

		scriptLoader.getScripts().forEach {

			val manifest = it.javaClass.getDeclaredAnnotation(ScriptManifest::class.java)

			val formattedScriptName = " ${manifest.name} - [ author = ${manifest.author}, category = ${manifest.category}, desc = ${manifest.description}, version = ${manifest.version}"

			val radioButton = JRadioButton(formattedScriptName)

			radioButton.addMouseListener(this)

			scriptButtons.putIfAbsent(radioButton, it)

			scriptToggles.add(radioButton)

			scriptDisplayPanel.add(radioButton)

		}

		add(scriptDisplayPanel)

	}

	override fun mouseClicked(e : MouseEvent?)
	{

		if (e?.source is JRadioButton)
		{
			val radio = e.source as JRadioButton

			val script = scriptButtons[radio] ?: return

			if (scriptExecutor.getScript() != script)
				scriptExecutor.setScript(script)

			scriptExecutor.toggleScript()

		}

	}

	override fun mouseReleased(e : MouseEvent?) {}

	override fun mouseMoved(e : MouseEvent?) {}

	override fun mouseEntered(e : MouseEvent?) {}

	override fun mouseDragged(e : MouseEvent?) {}

	override fun mouseExited(e : MouseEvent?) {}

	override fun mousePressed(e : MouseEvent?) {}

}