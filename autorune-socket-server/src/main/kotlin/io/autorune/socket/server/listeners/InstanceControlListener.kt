package io.autorune.socket.server.listeners

import com.corundumstudio.socketio.SocketIOServer
import com.google.gson.JsonParser
import io.autorune.client.AutoRune
import io.autorune.client.event.instance.AddInstanceEvent
import io.autorune.client.event.instance.RemoveInstanceEvent
import io.autorune.client.event.instance.SelectInstanceEvent
import io.autorune.client.instance.ClientInstanceRepo
import io.autorune.socket.server.Event
import io.autorune.socket.server.EventCommand
import io.autorune.socket.server.EventListener

class InstanceControlListener : EventListener(Event.INSTANCE_CONTROL)
{

	override fun build(server: SocketIOServer)
	{

		server.addEventListener(eventName, String::class.java) { _, data, _ ->

			val jsonObject = JsonParser.parseString(data).asJsonObject

			val command = EventCommand.getCommand(jsonObject.get("command").asString)

			val instanceId = jsonObject.get("instance_id").asInt

			when(command) {

				EventCommand.INSTANCE_SELECT -> AutoRune.eventBus.post(SelectInstanceEvent(ClientInstanceRepo.selectedInstance, instanceId))

				EventCommand.INSTANCE_ADD -> AutoRune.eventBus.post(AddInstanceEvent())

				EventCommand.INSTANCE_REMOVE -> AutoRune.eventBus.post(RemoveInstanceEvent(instanceId))

				else -> return@addEventListener

			}

		}

	}

}