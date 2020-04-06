package io.autorune.client.ui.component.instance

import java.awt.*
import javax.swing.plaf.basic.BasicTabbedPaneUI
import kotlin.math.max


class ClientInstanceTabbedPaneUI : BasicTabbedPaneUI()
{


	override fun paintFocusIndicator(g : Graphics?, tabPlacement : Int, rects : Array<out Rectangle>?, tabIndex : Int, iconRect : Rectangle?, textRect : Rectangle?, isSelected : Boolean)
	{

	}

	override fun paintTabBackground(g : Graphics, tabPlacement : Int, tabIndex : Int, x : Int, y : Int, w : Int, h : Int, isSelected : Boolean)
	{

		val graphics = g as Graphics2D

		if(tabIndex != 0)
		{

			graphics.color = lightHighlight

			if(isSelected)
				g.fillRect(x, y, w, h)

		}

	}

	override fun paintTabBorder(g : Graphics, tabPlacement : Int, tabIndex : Int, x : Int, y : Int, w : Int, h : Int, isSelected : Boolean)
	{

		if(tabIndex != 0)
		{

			g.drawLine(x, y, x + w, y) // top highlight

			g.drawLine(x, y, x, y + h) // left

			g.drawLine(x + w, y, x + w, y + h) // right

			g.drawLine(x, y + h, x + w, y + h) // bottom

		}

	}

	override fun paintContentBorderTopEdge(g : Graphics, tabPlacement : Int, selectedIndex : Int, x : Int, y : Int, w : Int, h : Int)
	{
		val selRect = if(selectedIndex < 0) null else getTabBounds(selectedIndex, calcRect)

		g.color = lightHighlight

		if(selectedIndex == 0 || selRect!!.y + selRect.height + 1 < y || selRect.x < x || selRect.x > x + w)
			g.drawLine(x, y, x + w - 2, y)
		else
		{

			if(selectedIndex == tabPane.selectedIndex)
			{

				g.color = Color.decode("#2B79D1")

				g.drawLine(x, y, x + w - 2, y)

			}

			g.color = lightHighlight

			g.drawLine(x, y, selRect.x - 1, y)

			if(selRect.x + selRect.width < x + w - 2)
				g.drawLine(selRect.x + selRect.width, y, x + w - 2, y)
			else
			{
				g.color = shadow
				g.drawLine(x + w - 2, y, x + w - 2, y)
			}

		}
	}

	override fun calculateMaxTabWidth(tabPlacement : Int) : Int
	{
		val metrics = fontMetrics
		val tabCount = tabPane.tabCount
		var result = 0
		for(tabIndex in 0 until tabCount)
			result = max(calculateTabWidth(tabPlacement, tabIndex, metrics), result)
		return result
	}

	override fun calculateTabWidth(tabPlacement : Int, tabIndex : Int, metrics : FontMetrics?) : Int
	{
		val tabInsets = getTabInsets(tabPlacement, tabIndex)
		var width = tabInsets.left + tabInsets.right + 3
		val tabComponent = tabPane.getTabComponentAt(tabIndex)
		if(tabComponent != null)
		{
			width += tabComponent.preferredSize.width
		}
		else
		{
			val icon = getIconForTab(tabIndex)
			if(icon != null)
				width += icon.iconWidth + textIconGap
			val title = tabPane.getTitleAt(tabIndex)
			width += fontMetrics.stringWidth(title)
		}
		return width
	}

}