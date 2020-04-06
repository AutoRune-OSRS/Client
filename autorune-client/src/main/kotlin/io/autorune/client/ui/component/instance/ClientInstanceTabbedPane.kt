package io.autorune.client.ui.component.instance

import com.google.common.eventbus.Subscribe
import io.autorune.client.AutoRune
import io.autorune.client.event.instance.*
import io.autorune.client.ui.frame.AutoRuneFrame
import io.autorune.client.ui.skin.ColorScheme
import io.autorune.client.ui.utilities.ImageUtil
import org.kxtra.swing.input.isMiddleButton
import org.pushingpixels.substance.internal.SubstanceSynapse
import java.awt.*
import java.awt.event.*
import java.lang.Boolean
import javax.swing.*

class ClientInstanceTabbedPane : JTabbedPane(), MouseListener
{

	private val addTabIcon = ImageIcon(ImageUtil.getResourceStreamFromClass(AutoRuneFrame::class.java, "add_tab.png"))

	private val addTabRolloverIcon = ImageIcon(ImageUtil.getResourceStreamFromClass(AutoRuneFrame::class.java, "add_tab_hover.png"))

	private val addTabJButton = JButton()

	init
	{

		addMouseListener(this)

		AutoRune.eventBus.register(this)

		setUI(ClientInstanceTabbedPaneUI())

		tabLayoutPolicy = SCROLL_TAB_LAYOUT

		addTabJButton.icon = addTabIcon
		addTabJButton.rolloverIcon = addTabRolloverIcon

		addTabJButton.toolTipText = "Add Client Instance"

		addTabJButton.putClientProperty(SubstanceSynapse.FLAT_LOOK, Boolean.TRUE)
		addTabJButton.isFocusable = false

		addTabJButton.isBorderPainted = false
		addTabJButton.isContentAreaFilled = false
		addTabJButton.isFocusPainted = false
		addTabJButton.isOpaque = false

		addTabJButton.addMouseListener(this)

		val defaultPanel = JPanel()

		defaultPanel.background = ColorScheme.MEDIUM_GRAY_COLOR

		addTab("", defaultPanel)

		setTabComponentAt(0, addTabJButton)

		setEnabledAt(0,  false)

	}

	override fun getMinimumSize() : Dimension
	{

		val tabHeight = 36
		val tabWidth = 8

		return Dimension(765 + tabWidth, 503 + tabHeight)

	}

	/**
	 * Adds a [ClientInstancePanel] to either the [instances] or for pending activation.
	 * @param instance [ClientInstancePanel] being instantiated.
	 * @param skipQueue Whether or not to queue activation for the [ClientInstancePanel].
	 */
	@Subscribe fun addInstance(event : AddedInstanceEvent)
	{

		val instance = event.instance

		instance.instanceId = tabCount

		val instancePanel = ClientInstancePanel(instance)

		addTab("Instance: ${instance.instanceId}", instancePanel)

		selectedIndex = instance.instanceId

		instancePanel.displayApplet()

		instance.client.isDebugMenuActionsEnabled = true

	}


	@Subscribe private fun removeInstance(event : RemoveInstanceEvent)
	{
		removeTabAt(event.instanceId)
	}

	@Subscribe fun selectInstance(event : SelectInstanceEvent)
	{
		if (event.oldInstanceId in 1 until tabCount)
		{
			val oldPanel = getComponentAt(event.oldInstanceId) as? ClientInstancePanel
			oldPanel?.debugWindow?.isVisible = false
			oldPanel?.scriptSelector?.isVisible = false

		}

		val newPanel = getComponentAt(event.newInstanceId) as? ClientInstancePanel

		newPanel?.debugWindow?.isVisible = true
		newPanel?.scriptSelector?.isVisible = true

	}

	override fun setSelectedIndex(index: Int) {

		if (index > 0)
			AutoRune.eventBus.post(SelectInstanceEvent(selectedIndex, index))

		super.setSelectedIndex(index)

	}

	override fun mouseClicked(e : MouseEvent)
	{

		val isTabbedPane = (e.source is ClientInstanceTabbedPane)

		val isJButton = (e.source is JButton)

		if(!isTabbedPane && !isJButton) return

		if(isTabbedPane)
		{

			val tabbedPane = e.source as ClientInstanceTabbedPane

			val index = tabbedPane.indexAtLocation(e.x, e.y);

			if(index == 0) return

			if (index > 0)
			{

				if(e.isMiddleButton)
					AutoRune.eventBus.post(RemoveInstanceEvent(index))

			}


		}
		else if(isJButton)
		{

			AutoRune.eventBus.post(AddInstanceEvent())

		}

	}

	override fun mouseReleased(e : MouseEvent)
	{

		val isJButton = (e.source is JButton)

		if(!isJButton) return

		addTabJButton.icon = addTabIcon

	}

	override fun mouseEntered(e : MouseEvent?)
	{

	}

	override fun mouseExited(e : MouseEvent)
	{

	}

	override fun mousePressed(e : MouseEvent)
	{

		val isJButton = (e.source is JButton)

		if(!isJButton) return

		addTabJButton.icon = addTabRolloverIcon

	}

}