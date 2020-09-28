package demo.math

import demo.math.Vector2.Companion.maximum
import demo.math.Vector2.Companion.minimum

data class Edge(val start: Vector2, val end: Vector2) {
    val length = (end - start).length
    val direction = end - start
    val middle = (start + end) / 2.0
    val normal = (direction / length).perp()

    val min get() = minimum(start, end)
    val max get() = maximum(start, end)
}
