package io.autorune.client.event.instance

import io.autorune.client.event.Event

data class SelectInstanceEvent(val oldInstanceId: Int, val newInstanceId: Int) : Event