package io.autorune.socket.server.emitters

import com.corundumstudio.socketio.SocketIOClient
import com.google.common.eventbus.Subscribe
import com.google.gson.JsonObject
import io.autorune.client.event.instance.AddedInstanceEvent
import io.autorune.client.event.instance.RemovedInstanceEvent
import io.autorune.client.event.instance.SelectedInstanceEvent
import io.autorune.socket.server.Event
import io.autorune.socket.server.EventCommand
import io.autorune.socket.server.SocketServer

object InstanceControlEmitter {

    @Subscribe
    fun instanceSelected(event: SelectedInstanceEvent)
    {
        emit(EventCommand.INSTANCE_SELECT, event.instanceId)
    }

    @Subscribe
    fun instanceAdded(event: AddedInstanceEvent)
    {
        emit(EventCommand.INSTANCE_ADD, event.instance.instanceId)
    }

    @Subscribe
    fun instanceRemoved(event: RemovedInstanceEvent)
    {
        emit(EventCommand.INSTANCE_REMOVE, event.instanceId)
    }

    fun emit(command: EventCommand, instanceId: Int, socketClient: SocketIOClient? = null) {

        val jsonResponse = JsonObject()
        jsonResponse.addProperty("command", command.commandString)
        jsonResponse.addProperty("instance_id", instanceId)

        if (socketClient != null)
            socketClient.sendEvent(Event.INSTANCE_CONTROL.eventName, jsonResponse.toString())
        else
            SocketServer.server.broadcastOperations.sendEvent(Event.INSTANCE_CONTROL.eventName, jsonResponse.toString())

    }


}