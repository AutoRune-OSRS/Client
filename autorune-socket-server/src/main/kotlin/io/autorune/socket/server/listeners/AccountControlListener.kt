package io.autorune.socket.server.listeners

import com.corundumstudio.socketio.SocketIOServer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.autorune.client.instance.ClientInstanceRepo
import io.autorune.socket.server.Event
import io.autorune.socket.server.EventCommand
import io.autorune.socket.server.EventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AccountControlListener : EventListener(Event.ACCOUNT_CONTROL)
{

	override fun build(server: SocketIOServer)
	{

		server.addEventListener(eventName, String::class.java) { _, data, _ ->

			val jsonObject = JsonParser.parseString(data).asJsonObject

			val command = EventCommand.getCommand(jsonObject.get("command").asString)

			val instanceId = jsonObject.get("instance_id").asInt

			var accountInfo: JsonElement = jsonObject.get("account_info")
			if (accountInfo.asString.contains("{"))
				accountInfo = JsonParser.parseString(accountInfo.asString).asJsonObject

			val clientInstance = ClientInstanceRepo.getInstanceFromId(instanceId)

			if (clientInstance != null)
			{

				when(command)
				{

					EventCommand.ACCOUNT_SELECT -> {
						accountInfo = accountInfo as JsonObject
						val email = accountInfo.get("email").asString
						val password = accountInfo.get("password").asString
						val pin = accountInfo.get("pin").asInt
						GlobalScope.launch { clientInstance.setAccount(email, password, pin) }
					}

					EventCommand.ACCOUNT_LOGIN -> GlobalScope.launch { clientInstance.login() }

					EventCommand.ACCOUNT_LOGOUT -> GlobalScope.launch { clientInstance.logout(false) }

					else -> return@addEventListener

				}

			}

		}

	}

}