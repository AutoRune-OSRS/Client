package io.autorune.script.loader

import java.io.File
import java.net.URL
import java.net.URLClassLoader

class ScriptClassLoader(val script : File, private val parentLoader : ClassLoader) : URLClassLoader(arrayOf<URL>(script.toURI().toURL()), null)
{

	@Throws(ClassNotFoundException::class)
	override fun loadClass(name : String?) : Class<*>
	{
		return try
		{
			super.loadClass(name)
		}
		catch(ex : ClassNotFoundException)
		{
			parentLoader.loadClass(name)
		}
	}

}