package io.autorune.client.ui.component.titlebar

import java.awt.*
import javax.swing.JComponent

class TitleBarLayoutManager(private val titleToolBar: JComponent, private val delegate: LayoutManager) : LayoutManager
{

	override fun addLayoutComponent(name : String, comp : Component)
	{
		delegate.addLayoutComponent(name, comp)
	}

	override fun removeLayoutComponent(comp : Component)
	{
		delegate.removeLayoutComponent(comp)
	}

	override fun preferredLayoutSize(parent : Container) : Dimension
	{
		return delegate.preferredLayoutSize(parent)
	}

	override fun minimumLayoutSize(parent : Container) : Dimension
	{
		return delegate.minimumLayoutSize(parent)
	}

	override fun layoutContainer(parent : Container)
	{
		delegate.layoutContainer(parent)
		val width = titleToolBar.preferredSize.width
		titleToolBar.setBounds(titleToolBar.width - 75 - width, 0, width, titleToolBar.height)
	}

}