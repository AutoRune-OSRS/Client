package io.autorune.client.ui.frame

import java.awt.Frame
import java.awt.event.WindowEvent
import javax.swing.JFrame
import kotlin.math.*

open class ContainableFrame : JFrame()
{

	var expandResizeType : ExpandResizeType? = null

	private var containedInScreen : FrameMode? = null

	private var expandedClientOppositeDirection = false

	fun setContainedInScreen(value : FrameMode?)
	{

		containedInScreen = value

		if(containedInScreen == FrameMode.ALWAYS)
		{
			// Reposition the frame if it is intersecting with the bounds
			this.setLocation(x, y)
			this.setBounds(x, y, width, height)
		}

	}

	override fun setLocation(x : Int, y : Int)
	{
		var x = x
		var y = y
		if(containedInScreen == FrameMode.ALWAYS)
		{
			val bounds = this.graphicsConfiguration.bounds
			x = max(x, bounds.getX().toInt())
			x = min(x, (bounds.getX() + bounds.getWidth() - width).toInt())
			y = max(y, bounds.getY().toInt())
			y = min(y, (bounds.getY() + bounds.getHeight() - height).toInt())
		}
		super.setLocation(x, y)
	}

	override fun setBounds(x : Int, y : Int, width : Int, height : Int)
	{
		var x = x
		var y = y
		var width = width
		var height = height
		if(containedInScreen == FrameMode.ALWAYS)
		{
			// XXX: this is wrong if setSize/resize is called because Component::resize sets private state that is read
			// in Window::setBounds
			val bounds = this.graphicsConfiguration.bounds
			width = min(width, width - bounds.getX().toInt() + x)
			x = max(x, bounds.getX().toInt())
			height = min(height, height - bounds.getY().toInt() + y)
			y = max(y, bounds.getY().toInt())
			width = min(width, (bounds.getX() + bounds.getWidth()).toInt() - x)
			height = min(height, (bounds.getY() + bounds.getHeight()).toInt() - y)
		}
		super.setBounds(x, y, width, height)
	}

	/**
	 * Expand frame by specified value. If the frame is going to be expanded outside of screen push the frame to
	 * the side.
	 *
	 * @param value size to expand frame by
	 */
	fun expandBy(value : Int)
	{
		if(isFullScreen) return
		var increment = value
		var forcedWidthIncrease = false
		if(expandResizeType === ExpandResizeType.KEEP_WINDOW_SIZE)
		{
			val minimumWidth = layout.minimumLayoutSize(this).width
			val currentWidth = width
			if(minimumWidth > currentWidth)
			{
				forcedWidthIncrease = true
				increment = minimumWidth - currentWidth
			}
		}
		if(forcedWidthIncrease || expandResizeType === ExpandResizeType.KEEP_GAME_SIZE)
		{
			val newWindowWidth = width + increment
			var newWindowX = x
			if(containedInScreen != FrameMode.NEVER)
			{
				val screenBounds = graphicsConfiguration.bounds
				val wouldExpandThroughEdge = x + newWindowWidth > screenBounds.getX() + screenBounds.getWidth()
				if(wouldExpandThroughEdge)
				{
					if(!isFrameCloseToRightEdge || isFrameCloseToLeftEdge)
					{
						// Move the window to the edge
						newWindowX = (screenBounds.getX() + screenBounds.getWidth()).toInt() - width
					}
					// Expand the window to the left as the user probably don't want the
					// window to go through the screen
					newWindowX -= increment
					expandedClientOppositeDirection = true
				}
			}
			setBounds(newWindowX, y, newWindowWidth, height)
		}
		revalidateMinimumSize()
	}

	/**
	 * Contract frame by specified value. If new frame size is less than it's minimum size, force the minimum size.
	 * If the frame was pushed from side before, restore it's original position.
	 *
	 * @param value value to contract frame by
	 */
	fun contractBy(value : Int)
	{
		if(isFullScreen) return
		revalidateMinimumSize()
		val screenBounds = graphicsConfiguration.bounds
		val wasCloseToLeftEdge = abs(x - screenBounds.getX()) <= SCREEN_EDGE_CLOSE_DISTANCE
		var newWindowX = x
		var newWindowWidth = width - value
		if(isFrameCloseToRightEdge && (expandedClientOppositeDirection || !wasCloseToLeftEdge))
		{
			// Keep the distance to the right edge
			newWindowX += value
		}
		if(expandResizeType === ExpandResizeType.KEEP_WINDOW_SIZE && newWindowWidth > minimumSize.width)
		{
			// The sidebar fits inside the window, do not resize and move
			newWindowWidth = width
			newWindowX = x
		}
		setBounds(newWindowX, y, newWindowWidth, height)
		validate()
		expandedClientOppositeDirection = false
	}

	/**
	 * Force minimum size of frame to be it's layout manager's minimum size
	 */
	fun revalidateMinimumSize()
	{
		minimumSize = layout.minimumLayoutSize(this)
	}

	private val isFullScreen : Boolean
		get() = extendedState and Frame.MAXIMIZED_BOTH == Frame.MAXIMIZED_BOTH

	private val isFrameCloseToLeftEdge : Boolean
		get()
		{
			val screenBounds = graphicsConfiguration.bounds
			return Math.abs(x - screenBounds.getX()) <= SCREEN_EDGE_CLOSE_DISTANCE
		}

	private val isFrameCloseToRightEdge : Boolean
		get()
		{
			val screenBounds = graphicsConfiguration.bounds
			return abs(x + width - (screenBounds.getX() + screenBounds.getWidth())) <= SCREEN_EDGE_CLOSE_DISTANCE
		}

	companion object
	{
		private const val SCREEN_EDGE_CLOSE_DISTANCE = 40
	}

	init
	{
		addWindowStateListener { windowEvent : WindowEvent ->
			if(windowEvent.newState == Frame.NORMAL)
			{
				revalidateMinimumSize()
			}
		}
	}
}