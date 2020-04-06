package io.autorune.interaction.device.mouse.path

import io.autorune.interaction.characteristic.Characteristics
import io.autorune.interaction.device.mouse.path.bezier.knotted.KnottedBezierPath
import io.autorune.interaction.device.mouse.path.linear.LinearPath
import io.autorune.interaction.device.mouse.path.noise.perlin.PerlinPath
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Canvas
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import javax.swing.JFrame
import javax.swing.WindowConstants

object MousePathGenerator {

    lateinit var canvas: Canvas

    private val characteristics = Characteristics.loadCharacteristics("testAccount")

    @JvmStatic
    fun main(args: Array<String>) {

        val frame = JFrame()
        frame.setLocationRelativeTo(null)
        frame.preferredSize = Dimension(785, 577)
        frame.size = frame.preferredSize
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        canvas = Canvas()
        canvas.preferredSize = Dimension(765, 503)
        canvas.size = frame.preferredSize
        canvas.background = Color.black

        val mouseLister = TestMouseListener()

        //canvas.addMouseListener(mouseLister)
        //canvas.addMouseMotionListener(mouseLister)

        frame.add(canvas)

        frame.isVisible = true

        val src = Point(100, 200)
        val dst = Point(500, 350)

        val linearPath = LinearPath(characteristics, src, dst).generate()

        val perlinPath = PerlinPath(linearPath).generate()

        val graphics = canvas.graphics

        GlobalScope.launch {

            graphics.color = Color.BLUE
            for (point in linearPath.points) {
                graphics.drawOval(point.x, point.y, 1, 1)
                delay(linearPath.timings[linearPath.points.indexOf(point)])
            }

            graphics.color = Color.PINK
            for (point in perlinPath.points) {
                graphics.drawOval(point.x, point.y, 1, 1)
                delay(perlinPath.timings[perlinPath.points.indexOf(point)])
            }

        }

    }

    class TestMouseListener : MouseListener, MouseMotionListener {

        private val moveQueue = mutableListOf<Point>()

        private var queuingPoints = false

        override fun mouseMoved(p0: MouseEvent?) {
            if (p0 != null && queuingPoints) {

                val graphics = canvas.graphics
                graphics.color = Color.PINK
                graphics.drawOval(p0.x, p0.y, 1, 1)

                moveQueue.add(Point(p0.x, p0.y))
            }
        }

        override fun mouseClicked(p0: MouseEvent?) {
            if (queuingPoints) {

                /*if (moveQueue.size < 2)
                    return

                val endPoints = moveQueue.filter { it == moveQueue.first() || it == moveQueue.last() }

                val bezierPath = BezierPath(LinearPath(characteristics, endPoints.first(), endPoints.last())).generate()
                val graphics = canvas.graphics
                graphics.color = Color.BLUE
                for (point in bezierPath.points)
                    graphics.drawOval(point.x, point.y, 1, 1)*/

                moveQueue.clear()
                queuingPoints = false
            } else
                queuingPoints = true
        }

        override fun mouseDragged(p0: MouseEvent?) {}
        override fun mouseReleased(p0: MouseEvent?) {}
        override fun mouseEntered(p0: MouseEvent?) {}
        override fun mouseExited(p0: MouseEvent?) {}
        override fun mousePressed(p0: MouseEvent?) {}

    }

}