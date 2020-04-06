package io.autorune.client

import com.google.common.eventbus.EventBus
import io.autorune.client.instance.ClientInstanceRepo
import io.autorune.client.ui.frame.AutoRuneFrame
import io.autorune.utilities.preferences.SystemPreferences

object AutoRune
{

	val eventBus = EventBus()

	val AUTORUNE_FRAME = AutoRuneFrame()

	init
	{

		eventBus.register(ClientInstanceRepo)

		SystemPreferences.getVanillaGamepackDirectory().toFile().deleteRecursively()

	}

}