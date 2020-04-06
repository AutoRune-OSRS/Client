package io.autorune.interaction.device.mouse.path.bezier

import java.awt.Point
import java.security.SecureRandom

operator fun Point.minus(other: Point) : Point { return Point(x-other.x, y-other.y) }
operator fun Point.plus(other: Point) : Point { return Point(x+other.x, y+other.y) }
operator fun Double.times(other: Point) : Point { return Point((toDouble()*(other.x.toDouble())).toInt(), (toDouble()*(other.y.toDouble())).toInt()) }

fun fetchDeviation(random: SecureRandom, maxDeviation: Int) : Int {

    if (maxDeviation == 0)
        return 0

    val isMound = random.nextBoolean()

    var deviation = random.nextInt(maxDeviation)
    if (!isMound)
        deviation = -deviation

    return deviation

}