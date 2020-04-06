package io.autorune.client.instance

import io.autorune.osrs.api.Client
import io.autorune.utilities.jar.JarDownloader
import io.autorune.utilities.jar.JarLoader
import io.autorune.utilities.preferences.SystemPreferences
import io.autorune.utilities.resource.JagexResourceFetcher


object InjectedInstanceFetcher {

    fun getInjectedInstance(members: Boolean): ClientInstance
    {

        val jagexResource = JagexResourceFetcher.getJagexResource(members)

        val jarInfo = JarDownloader.fetchJar(jagexResource.gamePack.url)

        val injectedGamepackFile = SystemPreferences.getInjectedJarLocation().toFile()

        val jarLoader = JarLoader()

        val classNodes = jarLoader.loadJar(injectedGamepackFile.toPath())

        val clientRevision = jarLoader.getRevision()

        val instanceLoader = ClientInstanceLoader(jagexResource.parameters)

        val clientInstance = instanceLoader.getClientInstance(injectedGamepackFile, classNodes)

        val vanillaPack = jarInfo.second

        (clientInstance.applet as? Client)?.initClassLoader(vanillaPack.toString())
        (clientInstance.applet as? Client)?.gameDrawingMode = 2

        clientInstance.codeBase = jagexResource.address
        clientInstance.osrsRevision = clientRevision

        return clientInstance
    }

}