package io.autorune.interaction.device.mouse.path.human

import io.autorune.interaction.characteristic.Characteristics
import io.autorune.interaction.device.mouse.path.MousePath
import io.autorune.interaction.device.mouse.path.bezier.knotted.KnottedBezierPath
import java.awt.Point
import java.security.SecureRandom

class HumanPath(private val characteristics: Characteristics, private val src: Point, private val dst: Point) : MousePath() {

    private val random = SecureRandom()

    override fun generate(): MousePath {

        val knottedBezierCurve = KnottedBezierPath(characteristics, src, dst).generate()


        return this

    }

}