package io.autorune.client.ui.component.titlebar

import com.google.common.collect.ComparisonChain
import io.autorune.client.ui.component.navigation.NavigationButton
import java.awt.*
import java.util.*
import java.util.function.*
import javax.swing.JPanel

class ClientTitleToolbar : JPanel()
{
	private val componentMap : MutableMap<NavigationButton, Component> = TreeMap(Comparator { a : NavigationButton, b : NavigationButton ->
		ComparisonChain
				.start()
				.compare(a.priority, b.priority)
				.compare(a.toolTip, b.toolTip)
				.result()
	})

	fun addComponent(button : NavigationButton, c : Component)
	{

		if(componentMap.put(button, c) == null)
		{
			update()
		}

	}

	fun removeComponent(button : NavigationButton)
	{

		if(componentMap.remove(button) != null)
		{
			update()
		}
	}

	private fun update()
	{
		removeAll()

		componentMap.values.forEach(Consumer { comp : Component? -> this.add(comp) })

		repaint()
	}

	companion object
	{
		private const val TITLEBAR_SIZE = 23
		private const val ITEM_PADDING = 4
	}

	/**
	 * Instantiates a new Client title toolbar.
	 */
	init
	{

		layout = object : LayoutManager2
		{

			override fun addLayoutComponent(name : String?, comp : Component?)
			{

			}

			override fun addLayoutComponent(comp : Component?, constraints : Any?)
			{
			}

			override fun removeLayoutComponent(comp : Component)
			{
			}

			override fun preferredLayoutSize(parent : Container) : Dimension
			{
				val width = parent.componentCount * (TITLEBAR_SIZE + ITEM_PADDING)
				return Dimension(width, TITLEBAR_SIZE)
			}

			override fun minimumLayoutSize(parent : Container) : Dimension
			{
				return preferredLayoutSize(parent)
			}

			override fun maximumLayoutSize(parent : Container) : Dimension
			{
				return preferredLayoutSize(parent)
			}

			override fun getLayoutAlignmentX(target : Container) : Float
			{
				return 0F
			}

			override fun getLayoutAlignmentY(target : Container) : Float
			{
				return 0F
			}

			override fun invalidateLayout(target : Container)
			{
			}

			override fun layoutContainer(parent : Container)
			{
				var x = 0
				for(c in parent.components)
				{
					x += ITEM_PADDING
					var height = c.preferredSize.height
					if(height > TITLEBAR_SIZE)
					{
						height = TITLEBAR_SIZE
					}
					c.setBounds(x, (TITLEBAR_SIZE - height) / 2, TITLEBAR_SIZE, height)
					x += TITLEBAR_SIZE
				}
			}
		}
	}
}