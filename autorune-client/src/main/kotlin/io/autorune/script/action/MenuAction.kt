package io.autorune.script.action

import io.autorune.osrs.api.entity.*
import io.autorune.osrs.api.widget.Widget
import io.autorune.script.action.builder.NpcAction
import io.autorune.script.action.type.MenuActionType
import io.autorune.script.action.type.MenuActionType.*
import io.autorune.script.api.client
import io.autorune.script.api.entity.npc.Npcs
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseEvent
import java.security.SecureRandom

class MenuAction private constructor(val type: MenuActionType?, val widget : Widget?, val interactingEntity : Entity?)
{

	private val random = SecureRandom()

	suspend fun performAction()
	{

		var bounds: Rectangle? = null

		when(type)
		{

			WIDGET_ACTION -> { bounds = widget?.bounds() }

			NPC_ACTION_0,
			NPC_ACTION_1,
			NPC_ACTION_2,
			NPC_ACTION_3,
			NPC_ACTION_4,
			EXAMINE_NPC -> { bounds = (interactingEntity as? Npc)?.fetchNpcConvexHull()?.bounds }

		}

		val n = NpcAction.Builder().action("Examine").interactWith((interactingEntity as Npc)).build()

		n.interact()

		/**
		 * null
		 * talk-to --- index 1 == NPC_ACTION_1
		 * null
		 * null
		 * examine
		 */

		if(bounds == null) return

		val minX = bounds.x
		val maxX = bounds.x + bounds.width

		val minY = bounds.y
		val maxY = bounds.y + bounds.height

		val randomPoint = Point(random.nextInt(maxX - minX + 1) + minX, random.nextInt(maxY - minY + 1) + minY)

		client().mouseListener.mouseMoved(MouseEvent(client().canvas, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, randomPoint.x, randomPoint.y, 0, false, MouseEvent.NOBUTTON))

		//client().sendMenuAction(0, 0, 1003, Npcs.getNpcs().indexOf(man), "Examine", ""/*"<col=ffff00>${definition.name}<col=c0ff00>  (level-${definition.combatLevel})"*/, randomPoint.x, randomPoint.y)

	}



	/*data class Builder(var type: MenuActionType? = null, var widget : Widget? = null, var interactingEntity : Entity? = null)
	{

		fun actionType(type : MenuActionType) = apply { this.type = type }

		fun interactWith(widget: Widget) = apply { this.widget = widget }

		fun interactWith(npc: Npc) = apply { this.interactingEntity = npc }

		fun interactWith(player: Player) = apply { this.interactingEntity = player }

		fun interactWith(obj: InteractiveObject) = apply { this.interactingEntity = obj.entity }

		fun build() = MenuAction(type, widget, interactingEntity)

	}*/

}