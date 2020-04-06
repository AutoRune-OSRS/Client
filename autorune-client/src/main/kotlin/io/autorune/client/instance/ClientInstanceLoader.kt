package io.autorune.client.instance

import io.autorune.osrs.api.Client
import org.objectweb.asm.tree.ClassNode
import java.applet.Applet
import java.io.File
import java.util.*

class ClientInstanceLoader(private val parameters: Map<String, String>) {

    fun getClientInstance(file: File, classes: ArrayList<ClassNode>): ClientInstance {

        val classLoader = ClientInstanceClassLoader(file, classes, parameters.getOrDefault("codebase", ""))

        val client = classLoader.loadClass("client")

        println("Loaded client class.")

        val instance = client.getDeclaredConstructor().newInstance()

        return ClientInstance(instance as Applet, instance as Client, parameters)

    }

}