package io.autorune

import io.autorune.client.AutoRune
import io.autorune.client.ui.configuration.AutoRuneUIConfig
import java.awt.Rectangle
import javax.swing.SwingUtilities

object Main
{

	@JvmStatic
	fun main(args : Array<String>)
	{

		SwingUtilities.invokeLater {
			initializeUI()
		}

		initializeServer()

	}

	private const val CLIENT_WELL_HIDDEN_MARGIN = 160
	private const val CLIENT_WELL_HIDDEN_MARGIN_TOP = 10

	private fun initializeUI()
	{
		AutoRuneUIConfig
		/*val frame = RootFrame
		frame.display()*/

		val frame = AutoRune.AUTORUNE_FRAME

		// Layout frame
		frame.pack()
		frame.revalidateMinimumSize()

		frame.setLocationRelativeTo(frame.owner)

		// If the frame is well hidden (e.g. unplugged 2nd screen),
		// we want to move it back to default position as it can be
		// hard for the user to reposition it themselves otherwise.
		// If the frame is well hidden (e.g. unplugged 2nd screen),
		// we want to move it back to default position as it can be
		// hard for the user to reposition it themselves otherwise.
		val clientBounds : Rectangle = frame.bounds
		val screenBounds : Rectangle = frame.graphicsConfiguration.bounds
		if(clientBounds.x + clientBounds.width - CLIENT_WELL_HIDDEN_MARGIN < screenBounds.getX() || clientBounds.x + CLIENT_WELL_HIDDEN_MARGIN > screenBounds.getX() + screenBounds.getWidth() || clientBounds.y + CLIENT_WELL_HIDDEN_MARGIN_TOP < screenBounds.getY() || clientBounds.y + CLIENT_WELL_HIDDEN_MARGIN > screenBounds.getY() + screenBounds.getHeight())
		{
			frame.setLocationRelativeTo(frame.owner)
		}

		frame.isVisible = true

	}

	private fun initializeServer() {

		//SocketServer.build()
		//SocketServer.launch()

	}

}