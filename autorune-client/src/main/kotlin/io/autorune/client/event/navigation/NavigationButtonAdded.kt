package io.autorune.client.event.navigation

import io.autorune.client.event.Event
import io.autorune.client.ui.component.navigation.NavigationButton

data class NavigationButtonAdded(val button: NavigationButton) : Event