package io.autorune.client.instance

import io.autorune.client.AutoRune
import io.autorune.client.event.instance.GameStateUpdateEvent
import io.autorune.client.interaction.MouseRecorder
import io.autorune.osrs.api.graphics.RasterProvider
import kotlinx.coroutines.runBlocking
import java.awt.*

class ClientInstanceCallbacks
{

	lateinit var clientInstance : ClientInstance

	private var lastRasterProvider : RasterProvider? = null
	private var lastGraphics : Graphics2D? = null

	val recorder = MouseRecorder()

	@Suppress("unused")
	fun clientTick() {

		val client = clientInstance.client

		val currentGameStatus = GameState.getStatus(client.gameState) ?: return

		if (currentGameStatus != clientInstance.gameStatus) {
			AutoRune.eventBus.post(GameStateUpdateEvent(clientInstance.instanceId, clientInstance.gameStatus, currentGameStatus))
			clientInstance.gameStatus = currentGameStatus
		}

	}

	private fun getGraphics(rasterProvider : RasterProvider) : Graphics2D?
	{
		if(lastGraphics == null || lastRasterProvider !== rasterProvider)
		{

			lastGraphics?.dispose()
			lastRasterProvider = rasterProvider
			lastGraphics = rasterProvider.image.graphics as Graphics2D
		}
		return lastGraphics
	}

	@Suppress("unused")
	fun drawRaster(rasterProvider : RasterProvider, graphics : Graphics?, x: Int, y: Int) {

		if (graphics == null)
			return

		val canvas = clientInstance.client.canvas

		if(!canvas.mouseListeners.contains(recorder))
			canvas.addMouseListener(recorder)
		if(!canvas.mouseMotionListeners.contains(recorder))
			canvas.addMouseMotionListener(recorder)
		if(!canvas.keyListeners.contains(recorder))
			canvas.addKeyListener(recorder)

		if(recorder.mouse == null) recorder.mouse = clientInstance.client.mouseListener

		val graphics2D = getGraphics(rasterProvider)

		if (graphics2D != null)
		{

			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

			if (clientInstance.debugExecutor.isRunning())
			{

				runBlocking(context = clientInstance.debugExecutor.scriptContext) {

					clientInstance.debugExecutor.onPaint(graphics2D)

				}

			}

			if (clientInstance.scriptExecutor.isRunning())
			{

				runBlocking(context = clientInstance.scriptExecutor.scriptContext) {

					clientInstance.scriptExecutor.onPaint(graphics2D)

					val color = graphics2D.color

					if(recorder.context == null) recorder.context = coroutineContext

					val client = clientInstance.client

					if(recorder.widgets == null) recorder.widgets = client.widgets

					if(recorder.rootWidget == null) recorder.rootWidget = client.rootWidget

					if(clientInstance.client.mouseListener != null)
					{
						graphics2D.color = Color.ORANGE
						graphics2D.fillOval(clientInstance.client.mouseListener.mouseX, clientInstance.client.mouseListener.mouseY, 10, 10)
						graphics2D.color = color

						recorder.paint(graphics2D)

					}
					else println("null")

				}

			}

		}

		graphics.drawImage(rasterProvider.image, 0, 0, canvas)

	}

}