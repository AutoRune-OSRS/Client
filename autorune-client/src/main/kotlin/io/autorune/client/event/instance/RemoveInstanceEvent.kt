package io.autorune.client.event.instance

import io.autorune.client.event.Event

data class RemoveInstanceEvent(var instanceId : Int) : Event