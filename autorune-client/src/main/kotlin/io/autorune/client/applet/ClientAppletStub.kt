package io.autorune.client.applet

import io.autorune.client.instance.ClientInstance
import java.applet.AppletContext
import java.applet.AppletStub
import java.net.URL

class ClientAppletStub(val instance: ClientInstance) : AppletStub
{

    val appletContext = ClientAppletContext(instance)

    override fun isActive(): Boolean {
        return true
    }

    override fun getCodeBase(): URL? {
        return URL(instance.codeBase)
    }

    override fun getParameter(name: String?): String? {
        return instance.parameters?.get(name)
    }

    override fun getAppletContext(): AppletContext? {
        return appletContext
    }

    override fun getDocumentBase(): URL {
        return URL(instance.codeBase)
    }

    override fun appletResize(width: Int, height: Int)
    {
        //println(((instance?.appletInstance as Client).gameShell))
        //(((instance?.appletInstance as Client).canvas) as Canvas).applySize(width, height)
    }

}