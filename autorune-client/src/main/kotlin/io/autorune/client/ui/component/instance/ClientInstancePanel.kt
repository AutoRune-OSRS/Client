package io.autorune.client.ui.component.instance

import io.autorune.client.instance.ClientInstance
import io.autorune.client.ui.component.debug.DebugToolWindow
import io.autorune.client.ui.component.script.ScriptSelector
import java.applet.Applet
import java.awt.*
import javax.swing.JPanel

data class ClientInstancePanel(val instance: ClientInstance) : JPanel(BorderLayout())
{

    private val defaultInstanceSize = Dimension(765, 503)

    val debugWindow = DebugToolWindow()

    val scriptSelector = ScriptSelector(instance.scriptExecutor)

    private var applet: Applet

    init
    {

        size = defaultInstanceSize
        preferredSize = defaultInstanceSize
        minimumSize = defaultInstanceSize
        background = Color.BLACK

        applet = instance.applet

        applet.layout = null
        applet.isVisible = true

        applet.size = Dimension(765, 503)

        add(applet, BorderLayout.CENTER)

    }

    fun displayApplet()
    {

        applet.init()
        applet.start()

    }

}