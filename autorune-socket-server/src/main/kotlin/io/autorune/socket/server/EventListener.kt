package io.autorune.socket.server

import com.corundumstudio.socketio.SocketIOServer

abstract class EventListener(event: Event)
{

	val eventName = event.eventName

	abstract fun build(server: SocketIOServer)

}