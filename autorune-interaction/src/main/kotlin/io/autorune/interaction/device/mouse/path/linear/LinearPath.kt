package io.autorune.interaction.device.mouse.path.linear

import io.autorune.interaction.characteristic.Characteristics
import io.autorune.interaction.device.mouse.path.MousePath
import io.autorune.interaction.device.mouse.path.MousePathContstants
import java.awt.Point
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

class LinearPath(private val characteristics: Characteristics, private val src: Point, private val dst: Point) : MousePath() {

    override fun generate() : LinearPath {

        val dpi = 400//characteristics.mouseDpi

        val pollingRate = 1000//characteristics.mousePollingRate

        val timePerInch = MousePathContstants.TPI_NUMERATOR.toDouble() / (dpi.toDouble() / MousePathContstants.PPI.toDouble())

        val inchesToMove = sqrt((dst.x - src.x).toDouble().pow(2) + (dst.y-src.y).toDouble().pow(2)) / MousePathContstants.PPI.toDouble()

        val totalTime = timePerInch * inchesToMove

        val pointCount = ((pollingRate.toDouble() / 1000.0) * totalTime).toInt() + 1

        points = mutableListOf()

        points.add(src)

        val xDelta = (dst.x - src.x).toDouble() / pointCount.toDouble()
        val yDelta = (dst.y - src.y).toDouble() / pointCount.toDouble()

        var lastX = src.x.toDouble()
        var lastY = src.y.toDouble()

        for (index in 1 until pointCount) {
            points.add(Point((lastX + xDelta).toInt(), (lastY + yDelta).toInt()))
            lastX += xDelta
            lastY += yDelta
        }

        points.add(dst)

        val timingArray = arrayOfNulls<Long>(points.size)
        timingArray.fill(ceil(totalTime / pointCount.toDouble()).toLong())

        timings = timingArray.filterNotNull().toMutableList()

        return this

    }

}