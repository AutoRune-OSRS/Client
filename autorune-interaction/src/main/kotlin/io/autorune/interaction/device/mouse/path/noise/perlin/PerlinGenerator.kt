package io.autorune.interaction.device.mouse.path.noise.perlin

import java.awt.Point
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class PerlinGenerator {

    fun generate2D(xIn: Double, yIn: Double) : Double {

        val s = (xIn+yIn)*F2 // Hairy factor for 2D
        var i = floor(xIn+s)
        var j = floor(yIn+s)
        val t = ((i)+(j))*G2
        val x0 = xIn-(i)+t // The x,y distances from the cell origin, unskewed.
        val y0 = yIn-(j)+t

        // For the 2D case, the simplex shape is an equilateral triangle.
        // Determine which simplex we are in.
        val i1: Int
        val j1: Int // Offsets for second (middle) corner of simplex in (i,j) coords
        if(x0>y0) { // lower triangle, XY order: (0,0)->(1,0)->(1,1)
            i1=1
            j1=0
        } else {    // upper triangle, YX order: (0,0)->(0,1)->(1,1)
            i1=0
            j1=1
        }
        // A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
        // a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
        // c = (3-sqrt(3))/6
        val x1 = x0 - (i1.toDouble()) + G2 // Offsets for middle corner in (x,y) unskewed coords
        val y1 = y0 - (j1.toDouble()) + G2
        val x2 = x0 - 1.0 + 2.0 * G2 // Offsets for last corner in (x,y) unskewed coords
        val y2 = y0 - 1.0 + 2.0 * G2
        // Work out the hashed gradient indices of the three simplex corners
        i = (i.toInt() and 255).toDouble()
        j = (j.toInt() and 255).toDouble()
        val gi0 = gradP[i.toInt()+perm[j.toInt()]]
        val gi1 = gradP[i.toInt()+i1+perm[j.toInt()+j1]]
        val gi2 = gradP[i.toInt()+1+perm[j.toInt()+1]]
        // Calculate the contribution from the three corners

        val n0 = getCornerValue(x0, y0, gi0)
        val n1 = getCornerValue(x1, y1, gi1)
        val n2 = getCornerValue(x2, y2, gi2)

        // Add contributions from each corner to get the final noise value.
        // The result is scaled to return values in the interval [-1,1].
        return 70.0 * (n0 + n1 + n2)

    }

    private fun getCornerValue(x: Double, y: Double, grad: Grad): Double {
        val t0 = 0.5 - x.pow(2)-y.pow(2)
        return if(t0<0.0) 0.0
        else t0.pow(3) * grad.dot2(x, y)  // (x,y) of grad3 used for 2D gradient
    }


    class Grad(val x: Int, val y: Int, val z: Int)

    private fun Grad.dot2(x: Double, y: Double) : Double {
        return this.x.toDouble()*x + this.y.toDouble()*y;
    }

    companion object {

        private val F2 = 0.5 * (sqrt(3.0) - 1.0)
        private val G2 = (3.0 - sqrt(3.0)) / 6.0

        private val p = listOf(151,160,137,91,90,15,
                131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
                190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
                88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
                77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
                102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
                135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
                5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
                223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
                129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
                251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
                49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
                138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180)

        val perm = IntArray(512)

        val gradP = Array(512) { Grad(0, 0, 0) }

        private var grad3 = listOf(Grad(1,1,0), Grad(-1,1,0), Grad(1,-1,0), Grad(-1,-1,0),
                Grad(1,0,1), Grad(-1,0,1), Grad(1,0,-1), Grad(-1,0,-1),
                Grad(0,1,1), Grad(0,-1,1), Grad(0,1,-1), Grad(0,-1,-1))

        init {

            var seed = 0.0

            if(seed > 0 && seed < 1)
                seed *= 65536

            seed = floor(seed)
            if(seed < 256)
                seed = (seed.toInt() or (seed.toInt() shl 8)).toDouble()

            for(i in 0 until 256) {

                val v: Int = if ((i and 1) == 1)
                    p[i] xor (seed.toInt() and 255)
                else
                    p[i] xor ((seed.toInt() shr 8) and 255)

                perm[i] = v
                perm[i + 256] = v

                gradP[i] = grad3[v % 12]
                gradP[i + 256] = grad3[v % 12]

            }

        }

    }

}