package io.autorune.client.ui.component.debug

import io.autorune.client.AutoRune
import io.autorune.script.debug.DebugState
import java.awt.Dimension
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.event.MouseInputListener

class DebugToolWindow : JFrame(), MouseInputListener
{

	private val debugPanel = JPanel()

	private val debugToggles = mutableListOf<JRadioButton>()

	init
	{

		isVisible = false

		preferredSize = Dimension(300, 300)
		size = preferredSize

		setLocation(AutoRune.AUTORUNE_FRAME.x - 300, AutoRune.AUTORUNE_FRAME.y);

		DebugState.javaClass.declaredFields.forEach {

			field ->

			if (field.name == "INSTANCE")
				return@forEach

			val radioButton = JRadioButton(field.name)
			radioButton.addMouseListener(this)
			debugToggles.add(radioButton)
			debugPanel.add(radioButton)

		}

		add(debugPanel)

	}

	override fun setVisible(b : Boolean)
	{

		if (b)
		{
			debugToggles.forEach {

				radio ->

				val debugWorkerName = radio.text

				val field = DebugState.javaClass.getDeclaredField(debugWorkerName)

				field.set(DebugState, radio.isSelected)

			}
		}

		super.setVisible(b)
	}

	override fun mouseClicked(e : MouseEvent?)
	{

		if (e?.source is JRadioButton)
		{
			val radio = e.source as JRadioButton

			val debugWorkerName = radio.text

			val field = DebugState.javaClass.getDeclaredField(debugWorkerName)

			field.set(DebugState, !(field.get(DebugState) as Boolean))

		}

	}

	override fun mouseReleased(e : MouseEvent?) {}

	override fun mouseMoved(e : MouseEvent?) {}

	override fun mouseEntered(e : MouseEvent?) {}

	override fun mouseDragged(e : MouseEvent?) {}

	override fun mouseExited(e : MouseEvent?) {}

	override fun mousePressed(e : MouseEvent?) {}

}