package io.autorune.script.action.builder

import io.autorune.script.action.type.MenuActionType
import java.awt.Point
import java.awt.Rectangle
import java.security.SecureRandom

abstract class MenuActionBuilder(val type: MenuActionType)
{

	//remove from each instantiation
	private val random = SecureRandom()

	abstract suspend fun interact()

	abstract fun generateActionIndexes() : IntArray

	fun generateRandomPoint(bounds : Rectangle?) : Point
	{

		if(bounds == null) throw Error("Interaction bounds should not be null.")

		val minX = bounds.x
		val maxX = bounds.x + bounds.width

		val minY = bounds.y
		val maxY = bounds.y + bounds.height

		return Point(random.nextInt(maxX - minX + 1) + minX, random.nextInt(maxY - minY + 1) + minY)

	}

}