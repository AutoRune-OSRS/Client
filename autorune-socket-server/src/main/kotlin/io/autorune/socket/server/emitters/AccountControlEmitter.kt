package io.autorune.socket.server.emitters

import com.corundumstudio.socketio.SocketIOClient
import com.google.common.eventbus.Subscribe
import com.google.gson.JsonObject
import io.autorune.client.event.account.AccountSelectedEvent
import io.autorune.client.event.instance.GameStateUpdateEvent
import io.autorune.client.instance.GameState
import io.autorune.socket.server.Event
import io.autorune.socket.server.EventCommand
import io.autorune.socket.server.SocketServer

object AccountControlEmitter {

    @Subscribe fun accountSelected(event: AccountSelectedEvent)
    {
        emit(EventCommand.ACCOUNT_SELECT, event.instanceId, event.credentials.email)
    }

    @Subscribe fun gameStateUpdate(event: GameStateUpdateEvent) {

        if (event.newState == GameState.LOGGED_IN && event.previousState == GameState.LOGGING_IN)
            emit(EventCommand.ACCOUNT_LOGIN, event.instanceId, "")

        if (event.newState == GameState.LOGIN_SCREEN && event.previousState == GameState.LOGGED_IN)
            emit(EventCommand.ACCOUNT_LOGOUT, event.instanceId, "")

        if (event.newState == GameState.LOGIN_SCREEN && event.previousState == GameState.LOGGING_IN) {
            //failed login
        }

    }

    fun emit(command: EventCommand, instanceId: Int, email: String, socketClient: SocketIOClient? = null) {

        val jsonResponse = JsonObject()
        jsonResponse.addProperty("command", command.commandString)
        jsonResponse.addProperty("instance_id", instanceId)
        jsonResponse.addProperty("email", email)

        if (socketClient != null)
            socketClient.sendEvent(Event.ACCOUNT_CONTROL.eventName, jsonResponse.toString())
        else
            SocketServer.server.broadcastOperations.sendEvent(Event.ACCOUNT_CONTROL.eventName, jsonResponse.toString())

    }


}