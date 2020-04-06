package io.autorune.client.event.instance

import io.autorune.client.event.Event

data class RemovedInstanceEvent(var instanceId : Int) : Event