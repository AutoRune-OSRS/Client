package io.autorune.client.event.account

import io.autorune.client.event.Event
import io.autorune.client.instance.account.AccountCredentials

data class AccountSelectedEvent(val instanceId: Int, val credentials: AccountCredentials) : Event