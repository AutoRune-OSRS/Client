package io.autorune.script.debug

import io.autorune.script.Script
import io.autorune.script.debug.worker.DisplayNpcConvexWorker
import io.autorune.script.worker.ScriptWorker

class DebugScript : Script()
{
	override fun workersToExecute() : List<ScriptWorker>
	{
		return listOf(DisplayNpcConvexWorker())
	}

	override fun onInitialize() {}

	override fun onStart() {}

	override fun scriptDelay() : Long
	{
		return 600
	}

	override suspend fun onLoop() {}

	override fun onStop() {}


}