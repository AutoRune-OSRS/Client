package io.autorune.client.event.sidebar

import io.autorune.client.event.Event
import io.autorune.client.ui.component.tools.ClientToolPanel

data class ContractSidebarEvent(val toolPanel: ClientToolPanel? = null) : Event