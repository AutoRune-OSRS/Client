package io.autorune.interaction.device.mouse.path.noise.perlin

import io.autorune.interaction.device.mouse.path.MousePath
import io.autorune.interaction.device.mouse.path.MousePathContstants
import java.awt.Point
import kotlin.math.ceil

class PerlinPath(private val mousePath: MousePath) : MousePath() {

    private val noiseGenerator = PerlinGenerator()

    override fun generate(): MousePath {

        timings = mousePath.timings
        points = mutableListOf()

        var currentTime = 0

        val middleIndex = mousePath.points.size / 2

        mousePath.points.forEachIndexed { index, point ->

            val a = currentTime + timings[index].toInt()

            val noise = generateNoise(a)

            points.add(Point(point.x+noise.x, point.y+noise.y))

            currentTime = a

        }

        return this

    }

    private fun generateNoise(a: Int) : Point {

        val noiseX = noiseGenerator.generate2D(0.0, a*0.0005)
        // We get another noise value for the y axis but because we don't want the same value than x, we need to use another value for the first parameter
        val noiseY = noiseGenerator.generate2D(1.0, a*0.0005)

        println("$noiseX , $noiseY")

        // Convert the noise values from [0, 1] to the size of the window
        val x = ceil(noiseX * MousePathContstants.PERLIN_MAX_DEVIATION.toDouble()).toInt()
        val y = ceil(noiseY * MousePathContstants.PERLIN_MAX_DEVIATION.toDouble()).toInt()

        return Point(x, y)

    }

}