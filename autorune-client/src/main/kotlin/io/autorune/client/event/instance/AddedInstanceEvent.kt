package io.autorune.client.event.instance

import io.autorune.client.event.Event
import io.autorune.client.instance.ClientInstance

data class AddedInstanceEvent(val instance: ClientInstance) : Event