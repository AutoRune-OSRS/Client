package io.autorune.interaction.device.mouse

import io.autorune.osrs.api.Client
import io.autorune.utilities.random.nextDouble
import java.awt.event.MouseEvent
import java.security.SecureRandom

object MouseController {

    private val MOUSE_MOVED = 503

    fun click(client: Client, x: Int, y: Int, prob: Double) {

        val mouseListener = client.mouseListener

        move(client, x, y, prob)

        //press and release here

    }

    fun move(client: Client, dstX: Int, dstY: Int, prob: Double) {

        val accurate = SecureRandom.getInstanceStrong().nextDouble(0.0, 1.0) <= prob

        val mouseListener = client.mouseListener

        val srcX = client.mouseListener.mouseX
        val srcY = client.mouseListener.mouseY

        val deltaX = dstX - srcX
        val deltaY = dstY - srcY

        val finalMoveEvent = MouseEvent(client.canvas, MOUSE_MOVED, System.currentTimeMillis(), 0, dstX, dstY, dstX, dstY, 0, false, 0)

        mouseListener.mouseMoved(finalMoveEvent)

        //move logic here

    }

}