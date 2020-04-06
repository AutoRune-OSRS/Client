package io.autorune.script.loader


import com.google.common.reflect.ClassPath
import io.autorune.script.Script
import io.autorune.script.annotation.ScriptManifest
import io.autorune.utilities.preferences.SystemPreferences
import java.nio.file.Files
import java.util.stream.*

class ScriptRepository
{

	lateinit var scripts: List<Script>

	fun initialize() {
		scripts = fetchAllScripts()
	}

	private fun fetchAllScripts(): List<Script> {

		val foundScripts = mutableListOf<Script>()

		val scriptDir = SystemPreferences.getScriptsDirectory()

		val scriptJarFiles = Files.walk(scriptDir).filter { Files.isRegularFile(it) && it.toFile().name.endsWith(".jar") }.map { it.toFile() }.collect(Collectors.toList())

		scriptJarFiles.forEach {

			scriptJarFile ->

			val classLoader = ScriptClassLoader(scriptJarFile, javaClass.classLoader)

			val classPath = ClassPath.from(classLoader)

			val scriptClassInfos = classPath.getAllClasses()

			scriptClassInfos.forEach {

				scriptClassInfo ->

				val script = scriptClassInfo.load()//.getDeclaredConstructor().newInstance()

				val hasScriptManifest = script.isAnnotationPresent(ScriptManifest::class.java)

				if (hasScriptManifest)
				{

					val scriptClazz = script.getDeclaredConstructor().newInstance()

					if (scriptClazz is Script)
						foundScripts.add(scriptClazz)

				}

			}

		}

		return foundScripts

	}

}