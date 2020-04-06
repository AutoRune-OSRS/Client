package io.autorune.client.event.instance

import io.autorune.client.event.Event
import io.autorune.client.instance.GameState

data class GameStateUpdateEvent(var instanceId : Int, var previousState: GameState, var newState: GameState) : Event