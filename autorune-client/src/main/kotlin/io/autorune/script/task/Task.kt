package io.autorune.script.task

abstract class Task
{

	abstract fun validate() : Boolean

	abstract suspend fun execute()

}