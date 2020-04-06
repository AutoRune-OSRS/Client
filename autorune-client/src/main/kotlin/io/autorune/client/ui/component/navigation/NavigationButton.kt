package io.autorune.client.ui.component.navigation

import io.autorune.client.ui.component.tools.ClientToolPanel
import java.awt.image.BufferedImage

data class NavigationButton(var icon : BufferedImage, var rolloverIcon : BufferedImage? = null, var tab : Boolean = true, var toolTip : String = "",
                            var onReady : () -> Unit, var selected : Boolean = false, var onClick : () -> Unit, var onSelect : () -> Unit,
                            var priority : Int = 0, var popup : Map<String, () -> Unit>?, var panel : ClientToolPanel? = null)