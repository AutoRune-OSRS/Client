package io.autorune.interaction.device.mouse.path

import java.awt.Point

abstract class MousePath() {

    lateinit var points: MutableList<Point>
    lateinit var timings: MutableList<Long>

    abstract fun generate() : MousePath

}