package demo.corridor

import demo.math.Edge
import demo.math.Matrix2
import demo.math.Vector2
import java.awt.geom.Area
import java.awt.geom.Path2D

class Route(val vertices: Array<Vector2>) {

    val edges = vertices.toList().windowed(2) { (v0, v1) -> Edge(v0, v1) }.toTypedArray()

    val length get() = if (edges.isEmpty()) 0.0 else edges.map { it.length }.reduce { acc, cur -> acc + cur }

    fun expand(radius: Double): Array<Edge> =
            extractEdges(Path2D.Double(edges.map { capsulify(it, radius) }.reduce { acc, cur -> acc.add(cur); acc }))

    private fun extractEdges(result: Path2D.Double): Array<Edge> {
        val edges = mutableListOf<Edge>()
        var start = Vector2.ZERO
        var curr = Vector2.ZERO
        val dst = DoubleArray(6)
        val pathIterator = result.getPathIterator(null)
        while (!pathIterator.isDone) {
            when (pathIterator.currentSegment(dst)) {
                0 -> {
                    start = Vector2(dst[0], dst[1])
                    curr = start
                }
                1 -> {
                    val next = Vector2(dst[0], dst[1])
                    if (!curr.equals(next, 1.0)) {
                        edges += Edge(curr, next)
                        curr = next
                    }
                }
                4 -> {
                    edges += Edge(curr, start)
                }
            }
            pathIterator.next()
        }
        return edges.toTypedArray()
    }

    private fun capsulify(edge: Edge, radius: Double): Area {
        val area = generateCircularArea(radius, edge.start)
        area.add(generateCircularArea(radius, edge.end))
        area.add(generateRectangularArea(radius, edge))
        return area
    }

    private fun generateCircularArea(radius: Double, offset: Vector2): Area {
        val v = Vector2.XAXIS * radius
        val path = Path2D.Double()
        for (r in 0 until 360 step 10) {
            val p = Matrix2.rotation(toRadians(r.toDouble())) * v + offset
            if (r == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
        }
        path.closePath()
        return Area(path)
    }

    private fun generateRectangularArea(radius: Double, edge: Edge): Area {
        val start1 = edge.start - edge.normal * radius
        val start2 = edge.start + edge.normal * radius
        val end1 = edge.end - edge.normal * radius
        val end2 = edge.end + edge.normal * radius
        val path = Path2D.Double()
        path.moveTo(start1.x, start1.y)
        path.lineTo(start2.x, start2.y)
        path.lineTo(end2.x, end2.y)
        path.lineTo(end1.x, end1.y)
        path.closePath()
        return Area(path)
    }

    companion object {
        private const val DEG2RAD = Math.PI / 180.0
        private fun toRadians(degrees: Double) = degrees * DEG2RAD
    }
}