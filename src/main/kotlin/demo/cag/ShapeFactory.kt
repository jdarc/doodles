package demo.cag

import demo.math.Matrix2
import demo.math.Vector2
import java.awt.geom.Arc2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Path2D

object ShapeFactory {

    fun triangle(width: Double, height: Double): Geometry {
        val w = width / 2.0
        val h = height / 3.0
        val shape = Path2D.Double()
        shape.moveTo(-w, -h)
        shape.lineTo(w, -h)
        shape.lineTo(0.0, height - h)
        shape.closePath()
        return Geometry(shape)
    }

    fun rectangle(width: Double, height: Double): Geometry {
        val w = width / 2.0
        val h = height / 2.0
        val shape = Path2D.Double()
        shape.moveTo(-w, -h)
        shape.lineTo(w, -h)
        shape.lineTo(w, h)
        shape.lineTo(-w, h)
        shape.closePath()
        return Geometry(shape)
    }

    fun pentagon(radius: Double): Geometry {
        val v = Vector2(radius, 0.0)
        val circle = Path2D.Double()
        circle.moveTo(radius, 0.0)
        val steps = 5
        for (i in 1 until steps) {
            val p = Matrix2.rotation(2 * Math.PI / steps * i) * v
            circle.lineTo(p.x, p.y)
        }
        circle.closePath()
        return Geometry(circle)
    }

    fun circle(radius: Double): Geometry {
        val shape = Path2D.Double()
        shape.append(Ellipse2D.Double(-radius, -radius, radius * 2.0, radius * 2.0), false)
        return Geometry(shape)
    }

    fun capsule(width: Double, height: Double): Geometry {
        val w = width / 2.0
        val h = height / 2.0
        val result = Path2D.Double()
        result.append(Arc2D.Double(w, h - w, width, width, 0.0, -180.0, Arc2D.OPEN), false)
        result.append(Arc2D.Double(w, w - h, width, width, 180.0, -180.0, Arc2D.OPEN), true)
        result.closePath()
        return Geometry(result)
    }
}