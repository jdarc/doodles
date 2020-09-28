package demo.cag

import demo.math.Vector2
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Path2D
import java.awt.geom.Rectangle2D

class Geometry(private val shape: Path2D.Double) {

    val bounds get() = shape.bounds2D

    var position
        get() = Vector2(bounds.centerX, bounds.centerY)
        set(value) {
            val cog = value - Vector2(bounds.centerX, bounds.centerY)
            shape.transform(AffineTransform.getTranslateInstance(cog.x, cog.y))
        }

    var angle = 0.0
        set(value) {
            shape.transform(AffineTransform.getRotateInstance(value - angle))
            field = value
        }

    fun intersects(r: Rectangle2D) = shape.intersects(r)

    fun contains(x: Double, y: Double) = shape.contains(x, y)

    fun draw(g: Graphics2D) = g.draw(shape)

    fun fill(g: Graphics2D) = g.fill(shape)

    companion object {

        fun add(first: Geometry, other: Geometry) = bool(first, other) { a, b -> a.add(b); a }

        fun subtract(first: Geometry, other: Geometry) = bool(first, other) { a, b -> a.subtract(b); a }

        fun intersect(first: Geometry, other: Geometry) = bool(first, other) { a, b -> a.intersect(b); a }

        private fun bool(first: Geometry, other: Geometry, op: (Area, Area) -> Area): Geometry {
            val result = Path2D.Double()
            result.append(op(Area(first.shape), Area(other.shape)), false)
            return Geometry(result)
        }
    }
}