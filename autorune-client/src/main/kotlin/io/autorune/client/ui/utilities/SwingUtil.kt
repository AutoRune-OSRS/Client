package io.autorune.client.ui.utilities

import io.autorune.client.ui.component.navigation.NavigationButton
import org.pushingpixels.substance.internal.SubstanceSynapse
import java.awt.image.BufferedImage
import java.lang.Boolean
import java.util.function.*
import javax.swing.*

object SwingUtil
{

	fun createSwingButton(navigationButton : NavigationButton, iconSize : Int, specialCallback : BiConsumer<NavigationButton, JButton>?) : JButton
	{

		val scaledImage : BufferedImage = if(iconSize > 0) ImageUtil.resizeImage(navigationButton.icon, iconSize, iconSize) else navigationButton.icon

		val button = JButton()
		button.setSize(scaledImage.width, scaledImage.height)
		button.toolTipText = navigationButton.toolTip
		button.icon = ImageIcon(scaledImage)
		button.putClientProperty(SubstanceSynapse.FLAT_LOOK, Boolean.TRUE)
		button.isFocusable = false

		button.isBorderPainted = false
		button.isContentAreaFilled = false
		button.isFocusPainted = false
		button.isOpaque = false

		if(navigationButton.rolloverIcon != null)
		{

			button.rolloverIcon = ImageIcon(navigationButton.rolloverIcon)

			button.pressedIcon = ImageIcon(navigationButton.rolloverIcon)

		}

		button.addActionListener {
			specialCallback?.accept(navigationButton, button)
			navigationButton.onClick()
		}

		val popupMenu = JPopupMenu()

		navigationButton.popup?.forEach { (name, callback) ->

			val menuItem = JMenuItem(name)

			menuItem.addActionListener { callback() }

			popupMenu.add(menuItem)

		}

		button.componentPopupMenu = popupMenu

		navigationButton.onSelect()

		//button.doClick()

		return button

	}

}