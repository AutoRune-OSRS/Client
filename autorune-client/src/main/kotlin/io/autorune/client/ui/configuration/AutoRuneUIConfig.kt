package io.autorune.client.ui.configuration

import io.autorune.client.ui.frame.AutoRuneFrame
import io.autorune.client.ui.skin.ColorScheme
import io.autorune.client.ui.skin.SubstanceAutoRuneLookAndFeel
import io.autorune.client.ui.utilities.ImageUtil
import java.awt.Color
import java.awt.Dimension
import java.awt.image.BufferedImage
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.plaf.basic.BasicProgressBarUI


object AutoRuneUIConfig
{

    val ICON : BufferedImage = ImageUtil.getResourceStreamFromClass(AutoRuneFrame::class.java, "logo.png")

    init {
	    setConfigOptions()
    }

    private fun setConfigOptions() {

        // Force heavy-weight popups/tooltips.
        // Prevents them from being obscured by the game applet.
        // Force heavy-weight popups/tooltips.
        // Prevents them from being obscured by the game applet.
        ToolTipManager.sharedInstance().isLightWeightPopupEnabled = false
        ToolTipManager.sharedInstance().initialDelay = 300
        JPopupMenu.setDefaultLightWeightPopupEnabled(false)

        UIManager.put("Button.foreground", Color.WHITE)
        UIManager.put("MenuItem.foreground", Color.WHITE)
        UIManager.put("Panel.background", ColorScheme.DARK_GRAY_COLOR)

       // UIManager.put("ScrollBarUI", CustomScrollBarUI::class.java.getName())

        UIManager.put("TextField.selectionBackground", ColorScheme.BRAND_BLUE_TRANSPARENT)
        UIManager.put("TextField.selectionForeground", Color.WHITE)
        UIManager.put("FormattedTextField.selectionBackground", ColorScheme.BRAND_BLUE_TRANSPARENT)
        UIManager.put("FormattedTextField.selectionForeground", Color.WHITE)
        UIManager.put("TextArea.selectionBackground", ColorScheme.BRAND_BLUE_TRANSPARENT)
        UIManager.put("TextArea.selectionForeground", Color.WHITE)
        UIManager.put("ProgressBar.background", ColorScheme.BRAND_BLUE_TRANSPARENT.darker())
        UIManager.put("ProgressBar.foreground", ColorScheme.BRAND_BLUE)
        UIManager.put("ProgressBar.selectionBackground", ColorScheme.BRAND_BLUE)
        UIManager.put("ProgressBar.selectionForeground", Color.BLACK)
        UIManager.put("ProgressBar.border", EmptyBorder(0, 0, 0, 0))
        UIManager.put("ProgressBar.verticalSize", Dimension(12, 10))
        UIManager.put("ProgressBar.horizontalSize", Dimension(10, 12))
        UIManager.put("ProgressBarUI", BasicProgressBarUI::class.java.name)



        // Do not render shadows under popups/tooltips.
        // Fixes black boxes under popups that are above the game applet.
        // Do not render shadows under popups/tooltips.
// Fixes black boxes under popups that are above the game applet.
        System.setProperty("jgoodies.popupDropShadowEnabled", "false")

        // Do not fill in background on repaint. Reduces flickering when
        // the applet is resized.
        // Do not fill in background on repaint. Reduces flickering when
// the applet is resized.
        System.setProperty("sun.awt.noerasebackground", "true")

        UIManager.setLookAndFeel(SubstanceAutoRuneLookAndFeel())


    }

}