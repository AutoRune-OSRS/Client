package io.autorune.interaction.device.mouse.path.bezier

import io.autorune.interaction.characteristic.Characteristics
import io.autorune.interaction.device.mouse.path.MousePath
import io.autorune.interaction.device.mouse.path.MousePathContstants
import io.autorune.interaction.device.mouse.path.linear.LinearPath
import java.awt.Point
import java.security.SecureRandom
import kotlin.math.pow

class BezierPath(private val linearPath: LinearPath, private val maxDeviation: Int) : MousePath() {

    private val random = SecureRandom()

    private fun pointAtTime(t: Double, p0: Point, p1: Point, p2: Point) : Point {
        return p1 + ((1.0-t).pow(2)*(p0-p1)) + (t.pow(2)*(p2-p1))
    }

    override fun generate() : MousePath {

        var deviation = fetchDeviation(random, maxDeviation)

        val bezierControlPoints = BezierControl(linearPath, deviation).generate()

        val n: Int = bezierControlPoints.size
        if (n < 3) // Cannot create bezier with less than 3 points
            return linearPath

        val pointCount = linearPath.points.size

        val timeIncrement = 1.0 / (pointCount - 1).toDouble()

        points = mutableListOf(bezierControlPoints.first())

        for (i in 1 until pointCount - 1)
            points.add(pointAtTime(i.toDouble()*timeIncrement,
                    bezierControlPoints.first(),
                    bezierControlPoints[1],
                    bezierControlPoints.last()))

        points.add(bezierControlPoints.last())

        timings = linearPath.timings

        return this

    }

}