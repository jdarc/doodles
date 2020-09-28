package demo.math

import kotlin.math.cos
import kotlin.math.sin

data class Matrix2(val a: Double, val b: Double, val c: Double, val d: Double) {

    operator fun times(mat: Matrix2): Matrix2 {
        val m00 = a * mat.a + b * mat.c
        val m01 = a * mat.b + b * mat.d
        val m10 = c * mat.a + d * mat.c
        val m11 = c * mat.b + d * mat.d
        return Matrix2(m00, m01, m10, m11)
    }

    operator fun times(vec: Vector2): Vector2 {
        val x = a * vec.x + b * vec.y
        val y = c * vec.x + d * vec.y
        return Vector2(x, y)
    }

    companion object {

        fun rotation(radians: Double): Matrix2 {
            val cos = cos(radians)
            val sin = sin(radians)
            return Matrix2(cos, -sin, sin, cos)
        }
    }
}