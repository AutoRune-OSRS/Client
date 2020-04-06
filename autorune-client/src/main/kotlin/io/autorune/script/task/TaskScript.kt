package io.autorune.script.task

import io.autorune.script.Script
import io.autorune.script.worker.ScriptWorker

abstract class TaskScript : Script() {

	private lateinit var tasks: List<Task>

	abstract fun tasksToExecute() : List<Task>

	override fun onInitialize()
	{
		tasks = tasksToExecute()
	}

	override suspend fun onLoop()
	{
		tasks.filter { task -> task.validate() }.forEach { readyTask -> readyTask.execute() }
	}

	override fun onStart() {}

	override fun onStop() {}

	override fun workersToExecute() : List<ScriptWorker>
	{
		return emptyList()
	}

}