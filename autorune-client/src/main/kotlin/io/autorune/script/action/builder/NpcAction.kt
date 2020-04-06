package io.autorune.script.action.builder

import io.autorune.osrs.api.entity.Npc
import io.autorune.script.action.type.MenuActionType
import io.autorune.script.api.client
import io.autorune.script.api.entity.npc.Npcs

class NpcAction private constructor(private val builder: Builder) : MenuActionBuilder(builder.type)
{

	override suspend fun interact()
	{

		val randomPoint = generateRandomPoint(builder.npc.fetchNpcConvexHull()?.bounds)

		println(generateActionIndexes().toTypedArray().contentDeepToString())

		//client().sendMenuAction(0, 0, type.opcode, Npcs.indexOf(builder.npc), builder.action, "", randomPoint.x, randomPoint.y)

	}

	override fun generateActionIndexes() : IntArray
	{

		val actions = builder.npc.definition.actions

		val maxActions = actions.size

		val beginningIndex = 13

		val indexes = IntArray(maxActions)

		for(index in 0 until maxActions)
			indexes[index] = beginningIndex - index

		return indexes.reversedArray()

	}

	class Builder
	{

		var type = MenuActionType.NPC_ACTION_0

		lateinit var npc: Npc

		lateinit var action : String

		fun action(action: String) = apply { this.action = action }

		fun actionType(type : MenuActionType) = apply { this.type = type }

		fun interactWith(npc: Npc) = apply { this.npc = npc }

		fun build() = NpcAction(this)

	}

}