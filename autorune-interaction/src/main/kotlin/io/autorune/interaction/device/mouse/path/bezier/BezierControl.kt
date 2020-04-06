package io.autorune.interaction.device.mouse.path.bezier

import io.autorune.interaction.device.mouse.path.MousePath
import java.awt.Point

class BezierControl(private val path: MousePath, private val deviation: Int) {

    fun generate() : List<Point> {

        val points = path.points

        val pointCount = points.size

        if (pointCount < 3)
            return points

        val middleIndex = pointCount / 2

        val bezierPoints = listOf(points.first(), points[middleIndex], points.last())

        bezierPoints[1].x += deviation
        bezierPoints[1].y -= deviation

        return bezierPoints

    }

}