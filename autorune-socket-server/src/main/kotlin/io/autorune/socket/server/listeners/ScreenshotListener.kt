package io.autorune.socket.server.listeners

import com.corundumstudio.socketio.SocketIOServer
import com.google.gson.JsonParser
import io.autorune.client.instance.ClientInstanceRepo
import io.autorune.socket.server.Event
import io.autorune.socket.server.EventListener
import io.autorune.socket.server.SocketServer
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import javax.imageio.ImageIO


class ScreenshotListener : EventListener(Event.SCREENSHOT)
{

	override fun build(server: SocketIOServer)
	{

		server.addEventListener(eventName, String::class.java) { _, data, _ ->

			val jsonObject = JsonParser.parseString(data).asJsonObject

			val instanceId = jsonObject.get("instance_id").asInt

			val clientInstance = ClientInstanceRepo.getInstanceFromId(instanceId)

			val screenshot = clientInstance?.getScreenshotImage()

			val byteArrayOutputStream = ByteArrayOutputStream()
			ImageIO.write(screenshot, "png", byteArrayOutputStream)

			val imageBytes = byteArrayOutputStream.toByteArray()
			byteArrayOutputStream.flush()
			byteArrayOutputStream.close()

			SocketServer.server.broadcastOperations.sendEvent(eventName, imageBytes)

		}

	}

}