package demo.math

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

data class Vector2(val x: Double, val y: Double) {

    val length = sqrt(dot(this))

    operator fun times(scale: Double) = Vector2(x * scale, y * scale)

    operator fun div(scale: Double) = Vector2(x / scale, y / scale)

    operator fun plus(v: Vector2) = Vector2(x + v.x, y + v.y)

    operator fun minus(v: Vector2) = Vector2(x - v.x, y - v.y)

    operator fun unaryMinus() = Vector2(-x, -y)

    fun perp() = Vector2(-y, x)

    fun normalize() = this / length

    fun dot(v: Vector2) = x * v.x + y * v.y

    fun equals(other: Vector2, delta: Double = 0.00001): Boolean {
        val dx = abs(this.x - other.x)
        val dy = abs(this.y - other.y)
        return dx <= delta && dy <= delta
    }

    companion object {
        val ZERO = Vector2(0.0, 0.0)
        val XAXIS = Vector2(1.0, 0.0)
        val POSITIVE_INFINITY = Vector2(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
        val NEGATIVE_INFINITY = Vector2(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)
        fun minimum(v1: Vector2, v2: Vector2) = Vector2(min(v1.x, v2.x), min(v1.y, v2.y))
        fun maximum(v1: Vector2, v2: Vector2) = Vector2(max(v1.x, v2.x), max(v1.y, v2.y))
    }
}