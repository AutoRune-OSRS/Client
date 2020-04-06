package io.autorune.script.loader

import io.autorune.script.Script


class ScriptLoader
{

	private lateinit var scriptRepository : ScriptRepository

	init
	{

		setupScriptLoader()

	}

	private fun setupScriptLoader()
	{

		scriptRepository = ScriptRepository()
		scriptRepository.initialize()

	}

	fun getScripts() : List<Script> {

		return scriptRepository.scripts

	}

}