package io.autorune.socket.server.listeners;

import com.corundumstudio.socketio.SocketIOServer
import io.autorune.client.instance.ClientInstanceRepo
import io.autorune.client.instance.GameState
import io.autorune.socket.server.Event
import io.autorune.socket.server.EventCommand
import io.autorune.socket.server.EventListener
import io.autorune.socket.server.emitters.AccountControlEmitter
import io.autorune.socket.server.emitters.InstanceControlEmitter

class InitialSyncListener : EventListener(Event.INITIAL_SYNC)
{

    override fun build(server: SocketIOServer)
    {

        server.addEventListener(eventName, String::class.java) { socketClient, _, _ ->

            val clientInstances = ClientInstanceRepo.getAllInstances()

            for (instance in clientInstances) {

                InstanceControlEmitter.emit(EventCommand.INSTANCE_ADD, instance.instanceId, socketClient)

                if (ClientInstanceRepo.selectedInstance == instance.instanceId)
                    InstanceControlEmitter.emit(EventCommand.INSTANCE_SELECT, instance.instanceId, socketClient)

                val credentials = instance.accountProfile.getCredentials()

                if (credentials != null) {

                    AccountControlEmitter.emit(EventCommand.ACCOUNT_SELECT, instance.instanceId, credentials.email, socketClient)

                    val instanceStatus = GameState.getStatus(instance.client.gameState)

                    if (instanceStatus == GameState.LOGGED_IN)
                        AccountControlEmitter.emit(EventCommand.ACCOUNT_LOGIN, instance.instanceId, credentials.email, socketClient)
                    else
                        AccountControlEmitter.emit(EventCommand.ACCOUNT_LOGOUT, instance.instanceId, credentials.email, socketClient)

                }

            }

        }

    }

}