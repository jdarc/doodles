package demo.corridor

import demo.math.Edge
import demo.math.Vector2
import demo.math.Vector2.Companion.maximum
import demo.math.Vector2.Companion.minimum
import java.awt.*
import java.awt.geom.Line2D
import javax.swing.JPanel
import kotlin.math.floor
import kotlin.math.min

class EditorPanel : JPanel() {

    private var route = Route(emptyArray())
    private var radius = 1.0
    private var viewportScaling = this::adaptiveScale

    init {
        val gd = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
        val width = (gd.displayMode.width * 7) / 8
        val height = (gd.displayMode.height * 4) / 5
        preferredSize = Dimension(width, height)
        isOpaque = true
        isDoubleBuffered = true
        isFocusable = true
        changeRoute()
    }

    fun enableAdaptiveScaling(toggleOn: Boolean) {
        viewportScaling = if (toggleOn) this::adaptiveScale else this::constantScale
        repaint()
    }

    fun changeRoute(totalSteps: Int = 25, stepSize: Double = 75.0, stepVariance: Double = 0.2, maxAngle: Double = 1.0) {
        route = generateRoute(totalSteps, stepSize, stepVariance, maxAngle)
        repaint()
    }

    fun changeRadius(value: Double) {
        radius = value
        repaint()
    }

    override fun paint(g: Graphics) = withGraphics2D(g).run {
        if (route.length > 0.0) {

            val corridor = route.expand(radius)
            viewportScaling(corridor).apply {
                scale(this * 0.9, -this * 0.9)
                stroke = BasicStroke((2.0 / this).toFloat())
            }

            this.color = Color.BLACK
            drawEdges(this, route.edges)

            this.color = Color.RED
            drawNormals(this, route.edges)

            this.color = Color.WHITE
            drawVertices(this, route.vertices)

            this.color = Color.GREEN
            drawEdges(this, corridor)
        }
    }

    private fun drawEdges(g: Graphics2D, edges: Array<Edge>) =
            edges.forEach { g.draw(Line2D.Double(it.start.x, it.start.y, it.end.x, it.end.y)) }

    private fun drawNormals(g: Graphics2D, edges: Array<Edge>) =
            edges.forEach { (it.middle + it.normal * 10.0).apply { g.draw(Line2D.Double(it.middle.x, it.middle.y, x, y)) } }

    private fun drawVertices(g: Graphics2D, vertices: Array<Vector2>) =
            vertices.forEach { g.fillOval(floor(it.x - 2.0).toInt(), floor(it.y - 2.0).toInt(), 4, 4) }

    private fun generateRoute(totalSteps: Int, stepSize: Double, stepVariance: Double, maxAngle: Double): Route {
        val dogWalker = DogWalker(stepVariance, maxAngle)
        return Route(dogWalker.walkies(Vector2.ZERO, Vector2(1.0, 0.0), totalSteps, stepSize))
    }

    private fun constantScale(ignore: Array<Edge>) = 0.75

    private fun adaptiveScale(edges: Array<Edge>): Double {
        var min = Vector2.POSITIVE_INFINITY
        var max = Vector2.NEGATIVE_INFINITY
        edges.forEach {
            min = minimum(min, it.min)
            max = maximum(max, it.max)
        }
        val size = max - min
        return min(width / size.x, height / size.y)
    }

    private fun withGraphics2D(g: Graphics) = (g as Graphics2D).apply {
        background = Color.WHITE
        clearRect(0, 0, width, height)
        setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        translate(width / 2.0, height / 2.0)
        return this
    }
}
