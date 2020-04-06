package io.autorune.script

import io.autorune.script.worker.ScriptWorker
import java.awt.Graphics2D

abstract class Script
{

	var scriptStatus = ScriptStatus.IDLE

	abstract fun workersToExecute() : List<ScriptWorker>

	abstract fun onInitialize()

	abstract fun onStart()

	abstract fun scriptDelay() : Long

	abstract suspend fun onLoop()

	abstract fun onStop()

	open suspend fun onPaint(graphics: Graphics2D) {

	}

}