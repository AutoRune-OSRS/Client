package io.autorune.script.worker

import java.awt.Graphics2D

abstract class ScriptWorker
{

	fun workerDelay() : Long {
		return 600
	}

	abstract suspend fun onLoop()

	abstract fun shouldExecute() : Boolean

	open suspend fun onPaint(graphics: Graphics2D) {

	}

}