package io.autorune.client.ui.frame

import io.autorune.client.AutoRune
import io.autorune.client.event.sidebar.ToggleSidebarEvent
import io.autorune.client.ui.configuration.AutoRuneUIConfig
import io.autorune.client.ui.frame.container.RootContentContainer
import java.awt.*
import javax.swing.*


class AutoRuneFrame : ContainableFrame()
{

	init
	{

		layeredPane.cursor = Cursor.getDefaultCursor()

		setLocationRelativeTo(owner)

		iconImage = AutoRuneUIConfig.ICON

		preferredSize = Dimension(785, 577)

		size = preferredSize

		isResizable = true

		isUndecorated = true

		isResizable = true

		getRootPane().windowDecorationStyle = JRootPane.FRAME

		add(RootContentContainer(this))

		updateFrameConfig()

		AutoRune.eventBus.post(ToggleSidebarEvent())

	}

	private fun updateFrameConfig()
	{

		expandResizeType = ExpandResizeType.KEEP_GAME_SIZE

		var containFrameMode : FrameMode = FrameMode.RESIZING

		// When native window decorations are enabled we don't have a way to receive window move events
		// so we can't contain to screen always.
		if(containFrameMode === FrameMode.ALWAYS)
			containFrameMode = FrameMode.RESIZING

		setContainedInScreen(containFrameMode)

	}

}