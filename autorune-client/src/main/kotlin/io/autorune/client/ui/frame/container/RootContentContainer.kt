package io.autorune.client.ui.frame.container

import io.autorune.client.AutoRune
import io.autorune.client.event.sidebar.ToggleSidebarEvent
import io.autorune.client.ui.component.navigation.NavigationButton
import io.autorune.client.ui.frame.AutoRuneFrame
import io.autorune.client.ui.component.instance.ClientInstanceTabbedPane
import io.autorune.client.ui.component.navigation.NavigationContainer
import io.autorune.client.ui.component.sidebar.ClientSidebar
import io.autorune.client.ui.component.titlebar.ClientTitleToolbar
import io.autorune.client.ui.utilities.ImageUtil
import io.autorune.client.ui.utilities.SwingUtil
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities
import org.pushingpixels.substance.internal.utils.SubstanceTitlePaneUtilities
import java.awt.*
import javax.swing.*

class RootContentContainer(val frame: AutoRuneFrame) : JPanel()
{

	private val clientInstanceTabbedPane = ClientInstanceTabbedPane()

	internal val navContainer = NavigationContainer(this)

	internal val titleToolbar = ClientTitleToolbar()

	internal var pluginToolbar = ClientSidebar(this)

	private val sidebarOpenIcon = ImageUtil.getResourceStreamFromClass(AutoRuneFrame::class.java, "expand.png")

	private val sidebarOpenRolloverIcon = ImageUtil.getResourceStreamFromClass(AutoRuneFrame::class.java, "expand_hover.png")

	private val sidebarClosedIcon = ImageUtil.flipImage(sidebarOpenIcon, horizontal = true, vertical = false)

	private val sidebarClosedRolloverIcon = ImageUtil.flipImage(sidebarOpenRolloverIcon, horizontal = true, vertical = false)

	private val sidebarNavigationButton = NavigationButton(sidebarOpenIcon, sidebarOpenRolloverIcon, true, "", { }, false, this::toggle, { }, 100, null, navContainer.currentPanel)

	private val sidebarNavigationJButton = SwingUtil.createSwingButton(sidebarNavigationButton, 0, null)

	private fun toggle()
	{

		AutoRune.eventBus.post(ToggleSidebarEvent(navContainer.currentPanel))

	}

	internal fun updateSideBarIcon()
	{

		if(pluginToolbar.isExpanded)
		{
			sidebarNavigationButton.icon = sidebarOpenIcon
			sidebarNavigationButton.rolloverIcon = sidebarOpenRolloverIcon
			sidebarNavigationJButton.icon = ImageIcon(sidebarOpenIcon)
			sidebarNavigationJButton.rolloverIcon = ImageIcon(sidebarOpenRolloverIcon)
			sidebarNavigationJButton.pressedIcon = ImageIcon(sidebarOpenRolloverIcon)
		}
		else
		{
			sidebarNavigationButton.icon = sidebarClosedIcon
			sidebarNavigationButton.rolloverIcon = sidebarClosedRolloverIcon
			sidebarNavigationJButton.icon = ImageIcon(sidebarClosedIcon)
			sidebarNavigationJButton.rolloverIcon = ImageIcon(sidebarClosedRolloverIcon)
			sidebarNavigationJButton.pressedIcon = ImageIcon(sidebarClosedRolloverIcon)
		}

	}

	init
	{

		AutoRune.eventBus.register(this)

		border = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.decode("#333232")),
				BorderFactory.createMatteBorder(2, 2, 0, 2, Color.decode("#1e1e1e")))

		layout = BoxLayout(this, BoxLayout.X_AXIS)

		add(clientInstanceTabbedPane)

		add(navContainer)

		val titleBar = SubstanceCoreUtilities.getTitlePaneComponent(frame)
		titleToolbar.putClientProperty(SubstanceTitlePaneUtilities.EXTRA_COMPONENT_KIND, SubstanceTitlePaneUtilities.ExtraComponentKind.TRAILING)
		titleBar.add(titleToolbar)

		val delegate = titleBar.layout
		titleBar.layout = object : LayoutManager
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
				val width = titleToolbar.preferredSize.width
				titleToolbar.setBounds(titleBar.width - 75 - width, 2, width, titleBar.height)
			}
		}

		titleToolbar.addComponent(sidebarNavigationButton, sidebarNavigationJButton)

	}

}