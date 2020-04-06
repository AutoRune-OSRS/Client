package io.autorune.client.interaction

import io.autorune.osrs.api.widget.Widget
import io.autorune.script.api.client
import kotlinx.coroutines.*
import java.awt.*
import java.awt.event.*
import kotlin.coroutines.CoroutineContext

class MouseRecorder(var mouse : io.autorune.osrs.api.devices.MouseListener? = null, var widgets : Array<Array<Widget>>? = null, var rootWidget: Int = 0) : MouseListener, MouseMotionListener, KeyListener
{

	var record = false

	var context : CoroutineContext? = null

	val recordedEvents = mutableListOf<MouseEvent>()

	val recordedWidgets = mutableMapOf<MouseEvent, Widget>()

	val recordedEventNoise = mutableListOf<Rectangle>()

	var currentWidget: Widget? = null

	override fun mouseReleased(e : MouseEvent)
	{

	}

	override fun mouseEntered(e : MouseEvent)
	{

	}

	override fun mouseClicked(e : MouseEvent)
	{

	}

	override fun mouseExited(e : MouseEvent)
	{

	}

	override fun mousePressed(e : MouseEvent)
	{

	}

	override fun mouseMoved(e : MouseEvent)
	{

		if(record)
		{

			if(widgets != null)
			{
				widgets?.forEach {

					if(it != null)
					{

						val iterator = it.iterator()

						var lastWidget : Widget? = null

						while(iterator.hasNext())
						{

							val next = iterator.next()

							if(next.isVisible && next.bounds().contains(e.point))
								lastWidget = next
						}

						if(lastWidget != null)
							recordedWidgets.putIfAbsent(e, lastWidget)

						return@forEach

					}

				}

			}

			/*if(interactingWidget != null)
				recordedEvents.putIfAbsent(e, interactingWidget)
			else println("no found widgets")*/

			recordedEvents.add(e)

			recordedEventNoise.add(Rectangle(e.x, e.y, 6, 2))

			e.consume()


		}
	}

	override fun mouseDragged(e : MouseEvent)
	{

	}

	override fun keyTyped(e : KeyEvent)
	{

	}

	override fun keyPressed(e : KeyEvent)
	{

//		println("${e.keyCode} : ${e.keyChar}")

		if(e.keyChar == 'd' && !record)
		{

			record = true

		}
		e.consume()

	}

	override fun keyReleased(e : KeyEvent)
	{

		if(e.keyChar == 'f' && record)
		{
			record = false
		}

		if(e.keyChar == 'r')
			launchRecordedEvents()

		e.consume()

	}

	private fun launchRecordedEvents()
	{

		GlobalScope.launch {


			for(i in 0 until recordedEvents.size)
			{

				val wait = recordedEvents[i].`when` - recordedEvents[i - if(i == 0) 0 else 1].`when`

				mouse?.mouseMoved(recordedEvents[i])

				println("current widget: ${recordedWidgets.get(recordedEvents[i])}")

				currentWidget = recordedWidgets[recordedEvents[i]]

				delay(wait)

			}

			recordedEvents.clear()

			recordedEventNoise.clear()

		}

	}

	fun paint(graphics : Graphics2D)
	{

		if(!record)
		{

			val noise = recordedEventNoise

			noise.forEach {

				graphics.color = Color.ORANGE

				graphics.fill(it)

				if(currentWidget != null)
					graphics.fill(currentWidget?.bounds())

			}

		}

	}

}