package io.autorune.client.applet

import io.autorune.client.instance.ClientInstance
import java.applet.*
import java.awt.Image
import java.awt.Toolkit
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL
import java.util.*

class ClientAppletContext(val instance: ClientInstance) : AppletContext {

    private val imageCache = HashMap<URL, WeakReference<Image>>()

    private val inputCache = Collections.synchronizedMap(HashMap<String, InputStream>(2))

    override fun getStreamKeys(): MutableIterator<String> {
        return Collections.unmodifiableSet(inputCache.keys).iterator()
    }

    override fun getApplet(name: String?): Applet
    {
        return instance.applet
    }

    override fun getImage(url: URL): Image? {
        synchronized(imageCache) {
            var ref: WeakReference<Image>? = imageCache[url]
            var img: Image? = null
            if(ref?.get() != null)
            {
                img = Toolkit.getDefaultToolkit().createImage(url)
                ref = WeakReference(img)
                imageCache[url] = ref
            }
            return img
        }
    }

    override fun getAudioClip(url: URL?): AudioClip {
        throw UnsupportedOperationException("NOT YET IMPLEMENTED getAudioClip=$url")
    }

    override fun getApplets(): Enumeration<Applet> {
        val applets = Vector<Applet>()
        applets.add(instance.applet)
        return applets.elements()
    }

    override fun showDocument(url: URL?) {

    }

    override fun showDocument(url: URL?, target: String?) {

    }

    override fun getStream(key: String?): InputStream? {
        return inputCache[key]
    }

    override fun setStream(key: String?, stream: InputStream?) {
        inputCache[key] = stream
    }

    override fun showStatus(status: String?) {

    }

}