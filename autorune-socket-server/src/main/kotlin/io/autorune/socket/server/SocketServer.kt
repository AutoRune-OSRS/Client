package io.autorune.socket.server

import com.corundumstudio.socketio.*
import io.autorune.client.AutoRune
import io.autorune.socket.server.emitters.AccountControlEmitter
import io.autorune.socket.server.emitters.InstanceControlEmitter
import io.autorune.socket.server.listeners.*


object SocketServer
{

	@JvmStatic
	lateinit var server: SocketIOServer

	fun build()
	{

		val configuration = Configuration()

		configuration.hostname = "localhost"

		configuration.port = 5150

		val socketConfig = SocketConfig()
		socketConfig.isReuseAddress = true

		configuration.socketConfig = socketConfig

		server = SocketIOServer(configuration)

		Runtime.getRuntime().addShutdownHook(Thread(server::stop));

		val listeners = listOf(
				InitialSyncListener(),
				AccountControlListener(),
				InstanceControlListener(),
				ScreenshotListener())

		for (listener in listeners)
			listener.build(server)

		registerEmitterEventBus()

	}

	private fun registerEmitterEventBus() {

		AutoRune.eventBus.register(AccountControlEmitter)
		AutoRune.eventBus.register(InstanceControlEmitter)

	}

	fun launch()
	{

		server.start()

	}

}