package io.autorune.script.debug.worker

import io.autorune.script.annotation.DebugManifest
import io.autorune.script.api.entity.npc.Npcs
import io.autorune.script.debug.DebugState
import io.autorune.script.worker.ScriptWorker
import java.awt.Graphics2D

@DebugManifest()
class DisplayNpcConvexWorker : ScriptWorker()
{


	override suspend fun onLoop()
	{


	}

	override fun shouldExecute() : Boolean
	{
		return DebugState.NPC_CONVEX
	}

	override suspend fun onPaint(graphics: Graphics2D)
	{

		Npcs.getNpcs().filterNotNull().forEach {

			val convexHull = it.fetchNpcConvexHull()

			if (convexHull != null)
				graphics.draw(convexHull)

		}

	}
}