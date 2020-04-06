package test


import io.autorune.osrs.api.entity.Npc
import io.autorune.script.Script
import io.autorune.script.action.builder.NpcAction
import io.autorune.script.annotation.ScriptManifest
import io.autorune.script.api.client
import io.autorune.script.api.entity.npc.Npcs
import io.autorune.script.worker.ScriptWorker
import kotlinx.coroutines.delay

@ScriptManifest(name = "Test", author = "Chk", version = 1.0, description = "testing")
class TestScript : Script()
{
	override fun workersToExecute() : List<ScriptWorker>
	{
		return emptyList()
	}

	override fun onInitialize()
	{

	}

	override fun onStart()
	{

	}

	override fun scriptDelay() : Long
	{
		return 3500
	}

	override suspend fun onLoop()
	{

		if (client().gameState == 30)
		{

			val expert = Npcs.getNpcs().filterNotNull().first { it.definition.name == "Master Chef" }

			val n = NpcAction.Builder().action("Examine").interactWith(expert).build()

			n.interact()

		}

		delay(300)

	}

	override fun onStop()
	{

	}

}