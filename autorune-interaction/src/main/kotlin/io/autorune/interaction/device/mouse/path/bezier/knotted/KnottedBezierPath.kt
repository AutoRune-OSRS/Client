package io.autorune.interaction.device.mouse.path.bezier.knotted

import io.autorune.interaction.characteristic.Characteristics
import io.autorune.interaction.device.mouse.path.MousePath
import io.autorune.interaction.device.mouse.path.MousePathContstants
import io.autorune.interaction.device.mouse.path.bezier.BezierPath
import io.autorune.interaction.device.mouse.path.bezier.fetchDeviation
import io.autorune.interaction.device.mouse.path.linear.LinearPath
import java.awt.Point
import java.security.SecureRandom

class KnottedBezierPath(private val characteristics: Characteristics, private val src: Point, private val dst: Point) : MousePath() {

    private val random = SecureRandom()

    override fun generate(): KnottedBezierPath {

        val bezierPath = BezierPath(LinearPath(characteristics, src, dst).generate(), MousePathContstants.BEZIER_MAX_DEVIATION).generate()

        val linearPoints = bezierPath.points

        val pointCount = bezierPath.points.size

        val knotCount = random.nextInt(9) + 1

        val knotInterval = ((pointCount - knotCount) / knotCount) - 1

        points = mutableListOf()
        timings = mutableListOf()

        var knotStart = linearPoints[0]
        var knotStartIndex = 0

        for (i in 0 until knotCount) {

            val knotEndIndex = if (i == knotCount - 1) pointCount - 1 else knotStartIndex + knotInterval
            val knotEnd = linearPoints[knotEndIndex]

            if (knotEnd != linearPoints.last()) {
                val deviation = fetchDeviation(random, MousePathContstants.KNOT_MAX_DEVIATION)
                knotEnd.x += deviation
                knotEnd.y -= deviation
            }

            val knotBezierPath = BezierPath(LinearPath(characteristics, knotStart, knotEnd).generate(), MousePathContstants.BEZIER_MAX_DEVIATION_KNOT).generate()

            points.addAll(knotBezierPath.points)
            timings.addAll(knotBezierPath.timings)

            knotStart = knotEnd
            knotStartIndex = knotEndIndex

        }

        return this

    }

}