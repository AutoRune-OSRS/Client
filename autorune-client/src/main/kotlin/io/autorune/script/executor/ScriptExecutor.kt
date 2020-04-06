package io.autorune.script.executor

import io.autorune.osrs.api.Client
import io.autorune.script.Script
import io.autorune.script.ScriptStatus
import io.autorune.script.api.ApiContext
import io.autorune.script.worker.ScriptWorker
import kotlinx.coroutines.*
import java.awt.Graphics2D
import kotlin.coroutines.CoroutineContext

class ScriptExecutor(private val client: Client)
{

	private var script : Script? = null

	private lateinit var workers: List<ScriptWorker>

	private var executorStatus = ScriptExecutorStatus.IDLE

	lateinit var scriptContext : CoroutineContext

	fun getScript() : Script? {
		return script
	}

	fun setScript(script : Script) {


		if (!script.javaClass.simpleName.contains("Debug"))
			println("setScript$client")

		this.script = script

		stopExecutor()

		this.workers = script.workersToExecute()

		script.onInitialize()

		startExecutor()
	}

	fun startExecutor() {

		val ctx = ApiContext(client)

		this.scriptContext = ctx

		GlobalScope.launch(context = ctx) {

			start(ctx)

		}

	}

	fun startScript() {

		if (script?.javaClass?.simpleName?.contains("Debug") == false)
			println("startScript$client")

		if (script == null || script?.scriptStatus == ScriptStatus.RUNNING)
			return

		if (script?.scriptStatus == ScriptStatus.PAUSED)
			script?.scriptStatus = ScriptStatus.RUNNING
		else
			script?.scriptStatus = ScriptStatus.STARTED
	}

	fun stopScript() {
		script?.scriptStatus = ScriptStatus.STOPPED
	}

	fun toggleScript() {

		if (script?.javaClass?.simpleName?.contains("Debug") == false)
			println("toggleScript$client")

		if (script == null)
			return

		if (script?.scriptStatus == ScriptStatus.IDLE)
			script?.scriptStatus = ScriptStatus.STARTED
		else
			script?.scriptStatus = ScriptStatus.STOPPED

	}

	fun pauseScript() {
		script?.scriptStatus = ScriptStatus.PAUSED
	}

	private suspend fun start(coroutineContext : CoroutineContext) = withContext(coroutineContext) {

		if (script?.javaClass?.simpleName?.contains("Debug") == false)
			println("start$client")

		if (executorStatus != ScriptExecutorStatus.RUNNING)
		{

			executorStatus = ScriptExecutorStatus.RUNNING

			launch {

				runExecutor()

			}

			launch {

				runScript()

			}

			/*workers.forEach {

				worker ->

				launch {

					runWorker(worker)

				}

			}*/

		}

	}

	fun stopExecutor() {

		if (script?.javaClass?.simpleName?.contains("Debug") == false)
			println("stop$client")

		executorStatus = ScriptExecutorStatus.STOPPED

	}

	private suspend fun runExecutor() {

		if (script?.javaClass?.simpleName?.contains("Debug") == false)
			println("runExecutor$client")

		while(executorStatus == ScriptExecutorStatus.RUNNING)
		{

			if(script?.scriptStatus == ScriptStatus.STARTED)
			{
				script?.onStart()
				script?.scriptStatus = ScriptStatus.RUNNING
			}

			if(script?.scriptStatus == ScriptStatus.STOPPED)
			{
				script?.onStop()
				script?.scriptStatus = ScriptStatus.IDLE
			}

			delay(250)

		}

	}

	private suspend fun runScript() {

		if (script?.javaClass?.simpleName?.contains("Debug") == false)
			println("runScript$client")

		while(executorStatus == ScriptExecutorStatus.RUNNING)
		{

			if (script?.scriptStatus == ScriptStatus.RUNNING) {

				var scriptDelay = 0L

				script?.scriptDelay().let {
					if(it != null)
					{
						scriptDelay = it
					}
				}

				script?.onLoop()

				delay(scriptDelay)

			} else
				delay(250)

		}

	}

	private suspend fun runWorker(worker: ScriptWorker) {

		if (script?.javaClass?.simpleName?.contains("Debug") == false)
			println("runWorker$client")

		while(executorStatus == ScriptExecutorStatus.RUNNING)
		{

			if (script?.scriptStatus == ScriptStatus.RUNNING) {

				var workerDelay = 0L

				if(worker.shouldExecute())
				{

					workerDelay = worker.workerDelay()

					worker.onLoop()

				}

				delay(workerDelay)

			} else
				delay(250)

		}

	}

	suspend fun onPaint(graphics2D : Graphics2D) {

		if (executorStatus != ScriptExecutorStatus.RUNNING || script == null)
			return

		if (script?.scriptStatus == ScriptStatus.RUNNING) {

			script?.onPaint(graphics2D)

			workers.filter { it.shouldExecute() }.forEach { worker ->
				worker.onPaint(graphics2D)
			}

		}

	}

	fun isRunning() : Boolean
	{
		return executorStatus == ScriptExecutorStatus.RUNNING
	}

}