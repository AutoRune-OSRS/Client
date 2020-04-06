package io.autorune.client.ui.component.tools

import java.awt.*

import kotlin.reflect.KFunction1

class DynamicGridLayout @JvmOverloads constructor(rows : Int = 1, cols : Int = 0, hgap : Int = 0, vgap : Int = 0) : GridLayout(rows, cols, hgap, vgap)
{

	override fun preferredLayoutSize(parent : Container) : Dimension
	{
		synchronized(parent.treeLock) { return calculateSize(parent, Component::getPreferredSize) }
	}

	override fun minimumLayoutSize(parent : Container) : Dimension
	{
		synchronized(parent.treeLock) { return calculateSize(parent, Component::getMinimumSize) }
	}

	override fun layoutContainer(parent : Container)
	{
		synchronized(parent.treeLock) {
			val insets : Insets = parent.insets
			val ncomponents : Int = parent.componentCount
			var nrows : Int = rows
			var ncols : Int = columns
			if(ncomponents == 0)
			{
				return
			}
			if(nrows > 0)
			{
				ncols = (ncomponents + nrows - 1) / nrows
			}
			else
			{
				nrows = (ncomponents + ncols - 1) / ncols
			}
			val hgap : Int = hgap
			val vgap : Int = vgap
			// scaling factors
			val pd : Dimension = preferredLayoutSize(parent)
			val parentInsets : Insets = parent.insets
			val wborder : Int = parentInsets.left + parentInsets.right
			val hborder : Int = parentInsets.top + parentInsets.bottom
			val sw : Double = (1.0 * parent.width - wborder) / (pd.width - wborder)
			val sh : Double = (1.0 * parent.height - hborder) / (pd.height - hborder)
			val w = IntArray(ncols)
			val h = IntArray(nrows)
			// calculate dimensions for all components + apply scaling
			for(i in 0 until ncomponents)
			{
				val r = i / ncols
				val c = i % ncols
				val comp : Component = parent.getComponent(i)
				val d : Dimension = comp.preferredSize
				d.width = (sw * d.width).toInt()
				d.height = (sh * d.height).toInt()
				if(w[c] < d.width)
				{
					w[c] = d.width
				}
				if(h[r] < d.height)
				{
					h[r] = d.height
				}
			}
			// Apply new bounds to all child components
			var c = 0
			var x : Int = insets.left
			while(c < ncols)
			{
				var r = 0
				var y : Int = insets.top
				while(r < nrows)
				{
					val i = r * ncols + c
					if(i < ncomponents)
					{
						parent.getComponent(i).setBounds(x, y, w[c], h[r])
					}
					y += h[r] + vgap
					r++
				}
				x += w[c] + hgap
				c++
			}
		}
	}

	/**
	 * Calculate outer size of the layout based on it's children and sizer
	 *
	 * @param parent parent component
	 * @param sizer  functioning returning dimension of the child component
	 * @return outer size
	 */
	private fun calculateSize(parent : Container, sizer : KFunction1<Component, Dimension>) : Dimension
	{
		val ncomponents : Int = parent.componentCount
		var nrows : Int = rows
		var ncols : Int = columns
		if(nrows > 0)
		{
			ncols = (ncomponents + nrows - 1) / nrows
		}
		else
		{
			nrows = (ncomponents + ncols - 1) / ncols
		}
		val w = IntArray(ncols)
		val h = IntArray(nrows)
		// Calculate dimensions for all components
		for(i in 0 until ncomponents)
		{
			val r = i / ncols
			val c = i % ncols
			val comp : Component = parent.getComponent(i)
			var d : Dimension
			//todo
			sizer.apply { d = comp.preferredSize }

			if(w[c] < d.width)
			{
				w[c] = d.width
			}
			if(h[r] < d.height)
			{
				h[r] = d.height
			}
		}
		// Calculate total width and height of the layout
		var nw = 0
		for(j in 0 until ncols)
		{
			nw += w[j]
		}
		var nh = 0
		for(i in 0 until nrows)
		{
			nh += h[i]
		}
		val insets : Insets = parent.insets
		// Apply insets and horizontal and vertical gap to layout
		return Dimension(
				insets.left + insets.right + nw + (ncols - 1) * hgap,
				insets.top + insets.bottom + nh + (nrows - 1) * vgap)
	}

}